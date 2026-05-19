package com.o3interfaces.electricitybill.presentation.screen

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * MVI one-shot side effects delivered via Channel: show a snackbar
 * message or signal that a reading was saved successfully.
 * */
sealed interface MeterReadingSideEffect {
    data class ShowSnackbar(val message: String) : MeterReadingSideEffect
    object SaveSuccess : MeterReadingSideEffect
}
