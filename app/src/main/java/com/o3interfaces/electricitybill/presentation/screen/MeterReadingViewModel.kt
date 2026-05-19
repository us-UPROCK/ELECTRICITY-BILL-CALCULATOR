package com.o3interfaces.electricitybill.presentation.screen

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.o3interfaces.electricitybill.domain.model.Resource
import com.o3interfaces.electricitybill.domain.usecase.CalculateElectricityBillUseCase
import com.o3interfaces.electricitybill.domain.usecase.GetHistoricalReadingsUseCase
import com.o3interfaces.electricitybill.domain.usecase.GetSlabConfigUseCase
import com.o3interfaces.electricitybill.domain.usecase.SaveReadingUseCase
import com.o3interfaces.electricitybill.domain.usecase.ValidateInputUseCase
import com.o3interfaces.electricitybill.domain.usecase.ValidationResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.async
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * MVI ViewModel. Processes user intents, orchestrates use cases (history and slab config
 * fetched in parallel), exposes StateFlow<MeterReadingUiState>, and emits
 * one-shot side effects via a Channel.
 * */
@HiltViewModel
class MeterReadingViewModel @Inject constructor(
    private val validateInput: ValidateInputUseCase,
    private val getHistoricalReadings: GetHistoricalReadingsUseCase,
    private val getSlabConfig: GetSlabConfigUseCase,
    private val calculateBill: CalculateElectricityBillUseCase,
    private val saveReading: SaveReadingUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(MeterReadingUiState())
    val uiState: StateFlow<MeterReadingUiState> = _uiState.asStateFlow()

    private val _sideEffects = Channel<MeterReadingSideEffect>(Channel.BUFFERED)
    val sideEffects = _sideEffects.receiveAsFlow()

    fun processIntent(intent: MeterReadingIntent) {
        when (intent) {
            is MeterReadingIntent.ServiceNumberChanged -> _uiState.value =
                _uiState.value.copy(serviceNumber = intent.value, serviceNumberError = null)

            is MeterReadingIntent.MeterReadingChanged -> _uiState.value =
                _uiState.value.copy(meterReading = intent.value, meterReadingError = null)

            MeterReadingIntent.Submit -> handleSubmit()
            MeterReadingIntent.Save -> handleSave()
        }
    }

    private fun handleSubmit() {
        val state = _uiState.value

        val validationResult = validateInput(
            serviceNumber = state.serviceNumber,
            meterReading = state.meterReading,
            previousReading = null
        )

        if (validationResult is ValidationResult.Invalid) {
            _uiState.value = state.copy(
                serviceNumberError = validationResult.serviceNumberError,
                meterReadingError = validationResult.meterReadingError
            )
            return
        }

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true)

            val historyDeferred = async { getHistoricalReadings(state.serviceNumber) }
            val slabsDeferred = async { getSlabConfig() }

            val historyResult = historyDeferred.await()
            val slabsResult = slabsDeferred.await()

            if (slabsResult is Resource.Error) {
                _uiState.value = _uiState.value.copy(isLoading = false)
                _sideEffects.send(MeterReadingSideEffect.ShowSnackbar(slabsResult.message))
                return@launch
            }

            val slabs = (slabsResult as Resource.Success).data
            val history = if (historyResult is Resource.Success) historyResult.data else emptyList()
            val previousRecord = history.firstOrNull()
            val previousReadingValue = previousRecord?.reading

            val currentReading = state.meterReading.toInt()

            val revalidation = validateInput(
                serviceNumber = state.serviceNumber,
                meterReading = state.meterReading,
                previousReading = previousReadingValue
            )

            if (revalidation is ValidationResult.Invalid) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    serviceNumberError = revalidation.serviceNumberError,
                    meterReadingError = revalidation.meterReadingError
                )
                return@launch
            }

            val consumption = if (previousReadingValue != null) {
                currentReading - previousReadingValue
            } else {
                currentReading
            }

            val billResult = calculateBill(consumption, slabs)

            _uiState.value = _uiState.value.copy(
                isLoading = false,
                screenMode = ScreenMode.Result,
                historicalReadings = history,
                billCalculation = billResult,
                previousReading = previousReadingValue
            )
        }
    }

    private fun handleSave() {
        val state = _uiState.value
        val bill = state.billCalculation ?: return

        viewModelScope.launch {
            _uiState.value = state.copy(isLoading = true)

            val result = saveReading(
                serviceNumber = state.serviceNumber,
                reading = state.meterReading.toInt(),
                cost = bill.totalCost
            )

            when (result) {
                is Resource.Success -> {
                    _sideEffects.send(MeterReadingSideEffect.SaveSuccess)
                    _uiState.value = MeterReadingUiState()
                }
                is Resource.Error -> {
                    _uiState.value = state.copy(isLoading = false)
                    _sideEffects.send(MeterReadingSideEffect.ShowSnackbar(result.message))
                }
                Resource.Loading -> Unit
            }
        }
    }
}
