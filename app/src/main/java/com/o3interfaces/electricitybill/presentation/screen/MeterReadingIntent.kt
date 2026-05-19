package com.o3interfaces.electricitybill.presentation.screen

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * MVI intent contract. Sealed interface defining all user actions:
 * field text changes, Submit to calculate, and Save to persist the result.
 * */
sealed interface MeterReadingIntent {
    data class ServiceNumberChanged(val value: String) : MeterReadingIntent
    data class MeterReadingChanged(val value: String) : MeterReadingIntent
    object Submit : MeterReadingIntent
    object Save : MeterReadingIntent
}
