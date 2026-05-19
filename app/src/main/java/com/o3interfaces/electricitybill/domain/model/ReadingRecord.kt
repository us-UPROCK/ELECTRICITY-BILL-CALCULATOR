package com.o3interfaces.electricitybill.domain.model

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Domain model for a saved meter reading: service number, reading value,
 * calculated bill cost, and the timestamp when it was recorded.
 * */
data class ReadingRecord(
    val id: Long = 0,
    val serviceNumber: String,
    val reading: Int,
    val cost: Double,
    val timestamp: Long = System.currentTimeMillis()
)
