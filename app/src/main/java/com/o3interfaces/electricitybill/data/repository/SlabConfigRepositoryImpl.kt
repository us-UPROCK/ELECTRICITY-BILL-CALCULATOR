package com.o3interfaces.electricitybill.data.repository

import com.o3interfaces.electricitybill.data.source.SlabConfigDataSource
import com.o3interfaces.electricitybill.domain.model.Resource
import com.o3interfaces.electricitybill.domain.model.SlabConfig
import com.o3interfaces.electricitybill.domain.repository.SlabConfigRepository
import javax.inject.Inject

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Concrete SlabConfigRepository implementation. Delegates to SlabConfigDataSource
 * and maps DTOs to domain SlabConfig models, wrapping the result in Resource.
 * */
class SlabConfigRepositoryImpl @Inject constructor(
    private val dataSource: SlabConfigDataSource
) : SlabConfigRepository {

    override suspend fun getSlabConfig(): Resource<List<SlabConfig>> {
        return try {
            val dto = dataSource.loadSlabConfig()
            val slabs = dto.slabs.map { SlabConfig(it.upToUnits, it.ratePerUnit) }
            Resource.Success(slabs)
        } catch (e: Exception) {
            Resource.Error(e.message ?: "Failed to load slab configuration")
        }
    }
}
