package com.o3interfaces.electricitybill.data.repository

import com.o3interfaces.electricitybill.data.local.dao.ReadingRecordDao
import com.o3interfaces.electricitybill.data.local.mapper.toDomain
import com.o3interfaces.electricitybill.data.local.mapper.toEntity
import com.o3interfaces.electricitybill.domain.model.ReadingRecord
import com.o3interfaces.electricitybill.domain.model.Resource
import com.o3interfaces.electricitybill.domain.repository.MeterReadingRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Concrete MeterReadingRepository implementation. Wraps Room DAO calls
 * with IO dispatcher, Resource state, and Timber logging for save operations.
 * */
class MeterReadingRepositoryImpl @Inject constructor(
    private val dao: ReadingRecordDao
) : MeterReadingRepository {

    override suspend fun getLastReading(serviceNumber: String): Resource<ReadingRecord?> =
        withContext(Dispatchers.IO) {
            try {
                Resource.Success(dao.getLastReading(serviceNumber)?.toDomain())
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Failed to fetch last reading")
            }
        }

    override suspend fun getLastThreeReadings(serviceNumber: String): Resource<List<ReadingRecord>> =
        withContext(Dispatchers.IO) {
            try {
                Resource.Success(dao.getLastThreeReadings(serviceNumber).map { it.toDomain() })
            } catch (e: Exception) {
                Resource.Error(e.message ?: "Failed to fetch readings")
            }
        }

    override suspend fun saveReading(
        serviceNumber: String,
        reading: Int,
        cost: Double
    ): Resource<Unit> = withContext(Dispatchers.IO) {
        try {
            val entity = ReadingRecord(
                serviceNumber = serviceNumber,
                reading = reading,
                cost = cost
            ).toEntity()
            dao.insertReading(entity)
            Timber.i("Saved reading: service=%s, reading=%d, cost=%.2f", serviceNumber, reading, cost)
            Resource.Success(Unit)
        } catch (e: Exception) {
            Timber.e(e, "Failed to save reading for %s", serviceNumber)
            Resource.Error(e.message ?: "Failed to save reading")
        }
    }
}
