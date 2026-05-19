package com.o3interfaces.electricitybill.domain.usecase

import timber.log.Timber
import javax.inject.Inject

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Input validation use case. Validates service number (exactly 10 alphanumeric chars)
 * and meter reading (positive integer, not less than previous). Returns a typed
 * ValidationResult with field-level error messages.
 * */
sealed class ValidationResult {
    object Valid : ValidationResult()
    data class Invalid(val serviceNumberError: String? = null, val meterReadingError: String? = null) : ValidationResult()
}

class ValidateInputUseCase @Inject constructor() {

    operator fun invoke(
        serviceNumber: String,
        meterReading: String,
        previousReading: Int? = null
    ): ValidationResult {
        var serviceNumberError: String? = null
        var meterReadingError: String? = null

        when {
            serviceNumber.isBlank() ->
                serviceNumberError = "Service number is required"
            serviceNumber.length != 10 ->
                serviceNumberError = "Service number must be 10 characters"
            !serviceNumber.matches(Regex("[A-Za-z0-9]{10}")) ->
                serviceNumberError = "Service number must be alphanumeric only"
        }

        when {
            meterReading.isBlank() ->
                meterReadingError = "Meter reading is required"
            !meterReading.matches(Regex("[0-9]+")) ->
                meterReadingError = "Meter reading must be a positive number"
            else -> {
                val reading = meterReading.toIntOrNull()
                when {
                    reading == null ->
                        meterReadingError = "Meter reading must be a positive number"
                    reading < 0 ->
                        meterReadingError = "Meter reading cannot be negative"
                    previousReading != null && reading < previousReading ->
                        meterReadingError =
                            "Reading ($reading) cannot be less than last recorded reading ($previousReading)"
                }
            }
        }

        return if (serviceNumberError == null && meterReadingError == null) {
            ValidationResult.Valid
        } else {
            Timber.d(
                "Validation failed: serviceNumber=%s, meterReading=%s",
                serviceNumberError,
                meterReadingError
            )
            ValidationResult.Invalid(serviceNumberError, meterReadingError)
        }
    }
}
