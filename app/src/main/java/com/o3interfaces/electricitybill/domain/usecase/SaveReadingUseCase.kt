package com.o3interfaces.electricitybill.domain.usecase

import com.o3interfaces.electricitybill.domain.model.Resource
import com.o3interfaces.electricitybill.domain.repository.MeterReadingRepository
import javax.inject.Inject

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Use case that persists a new meter reading and its calculated
 * cost to the repository for a given service number.
 * */
class SaveReadingUseCase @Inject constructor(
    private val repository: MeterReadingRepository
) {
    suspend operator fun invoke(
        serviceNumber: String,
        reading: Int,
        cost: Double
    ): Resource<Unit> = repository.saveReading(serviceNumber, reading, cost)
}
