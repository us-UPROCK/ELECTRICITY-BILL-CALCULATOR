package com.o3interfaces.electricitybill.domain.repository

import com.o3interfaces.electricitybill.domain.model.ReadingRecord
import com.o3interfaces.electricitybill.domain.model.Resource

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Repository interface for meter reading persistence. Defines contracts
 * to fetch the last reading, fetch the last three, and save a new record.
 * */
interface MeterReadingRepository {
    suspend fun getLastReading(serviceNumber: String): Resource<ReadingRecord?>
    suspend fun getLastThreeReadings(serviceNumber: String): Resource<List<ReadingRecord>>
    suspend fun saveReading(serviceNumber: String, reading: Int, cost: Double): Resource<Unit>
}
