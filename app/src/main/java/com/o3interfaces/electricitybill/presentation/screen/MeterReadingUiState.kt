package com.o3interfaces.electricitybill.presentation.screen

import com.o3interfaces.electricitybill.domain.model.BillCalculation
import com.o3interfaces.electricitybill.domain.model.ReadingRecord

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * MVI state holder. Immutable data class containing all UI state: input field values,
 * validation errors, loading flag, screen mode, historical readings, and the bill result.
 * */
enum class ScreenMode { Entry, Result }

data class MeterReadingUiState(
    val serviceNumber: String = "",
    val meterReading: String = "",
    val serviceNumberError: String? = null,
    val meterReadingError: String? = null,
    val isLoading: Boolean = false,
    val screenMode: ScreenMode = ScreenMode.Entry,
    val historicalReadings: List<ReadingRecord> = emptyList(),
    val billCalculation: BillCalculation? = null,
    val previousReading: Int? = null
)
