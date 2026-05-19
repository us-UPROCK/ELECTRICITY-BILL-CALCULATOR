package com.o3interfaces.electricitybill.domain.model

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Generic sealed class representing async operation states:
 * Loading, Success<T> carrying data, and Error carrying a message.
 * */
sealed class Resource<out T> {
    object Loading : Resource<Nothing>()
    data class Success<T>(val data: T) : Resource<T>()
    data class Error(val message: String, val code: Int? = null) : Resource<Nothing>()
}
