package com.o3interfaces.electricitybill.presentation.screen

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.animation.togetherWith
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.o3interfaces.electricitybill.presentation.screen.components.BillResultCard
import com.o3interfaces.electricitybill.presentation.screen.components.HistoryTable
import com.o3interfaces.electricitybill.presentation.screen.components.InputForm

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Root screen composable. Hosts a Scaffold with TopAppBar and SnackbarHost.
 * Uses AnimatedContent to transition between the Entry input form and the
 * Result view (history table + bill breakdown + Save button).
 * */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MeterReadingScreen(
    viewModel: MeterReadingViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(Unit) {
        viewModel.sideEffects.collect { effect ->
            when (effect) {
                is MeterReadingSideEffect.ShowSnackbar ->
                    snackbarHostState.showSnackbar(effect.message)
                MeterReadingSideEffect.SaveSuccess ->
                    snackbarHostState.showSnackbar("Reading saved successfully")
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Electricity Bill Calculator") },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.primaryContainer,
                    titleContentColor = MaterialTheme.colorScheme.onPrimaryContainer
                )
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        AnimatedContent(
            targetState = uiState.screenMode,
            transitionSpec = {
                (slideInVertically { it } + fadeIn()) togetherWith
                        (slideOutVertically { -it } + fadeOut())
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues),
            label = "screen_transition"
        ) { mode ->
            when (mode) {
                ScreenMode.Entry -> {
                    InputForm(
                        serviceNumber = uiState.serviceNumber,
                        meterReading = uiState.meterReading,
                        serviceNumberError = uiState.serviceNumberError,
                        meterReadingError = uiState.meterReadingError,
                        isLoading = uiState.isLoading,
                        onServiceNumberChange = {
                            viewModel.processIntent(MeterReadingIntent.ServiceNumberChanged(it))
                        },
                        onMeterReadingChange = {
                            viewModel.processIntent(MeterReadingIntent.MeterReadingChanged(it))
                        },
                        onSubmit = { viewModel.processIntent(MeterReadingIntent.Submit) },
                        modifier = Modifier.fillMaxSize()
                    )
                }

                ScreenMode.Result -> {
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .verticalScroll(rememberScrollState())
                            .padding(vertical = 16.dp)
                    ) {
                        HistoryTable(readings = uiState.historicalReadings)

                        Spacer(modifier = Modifier.height(12.dp))

                        uiState.billCalculation?.let { bill ->
                            BillResultCard(billCalculation = bill)
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = { viewModel.processIntent(MeterReadingIntent.Save) },
                            enabled = !uiState.isLoading,
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp)
                        ) {
                            Text("Save")
                        }
                    }
                }
            }
        }
    }
}
