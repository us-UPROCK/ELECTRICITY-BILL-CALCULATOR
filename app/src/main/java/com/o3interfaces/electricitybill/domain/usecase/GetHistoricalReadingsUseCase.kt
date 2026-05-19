package com.o3interfaces.electricitybill.domain.usecase

import com.o3interfaces.electricitybill.domain.model.ReadingRecord
import com.o3interfaces.electricitybill.domain.model.Resource
import com.o3interfaces.electricitybill.domain.repository.MeterReadingRepository
import javax.inject.Inject

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Use case that retrieves the last 3 saved meter readings
 * for a given service number from the repository.
 * */
class GetHistoricalReadingsUseCase @Inject constructor(
    private val repository: MeterReadingRepository
) {
    suspend operator fun invoke(serviceNumber: String): Resource<List<ReadingRecord>> =
        repository.getLastThreeReadings(serviceNumber)
}
