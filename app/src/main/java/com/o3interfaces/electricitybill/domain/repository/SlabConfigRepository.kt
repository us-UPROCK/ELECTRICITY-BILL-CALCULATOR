package com.o3interfaces.electricitybill.domain.repository

import com.o3interfaces.electricitybill.domain.model.Resource
import com.o3interfaces.electricitybill.domain.model.SlabConfig

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Repository interface for loading the configurable slab pricing table
 * used to calculate electricity consumption costs.
 * */
interface SlabConfigRepository {
    suspend fun getSlabConfig(): Resource<List<SlabConfig>>
}
