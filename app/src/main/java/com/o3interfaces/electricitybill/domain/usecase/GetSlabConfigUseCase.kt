package com.o3interfaces.electricitybill.domain.usecase

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
 * Use case that fetches the current slab pricing configuration
 * from SlabConfigRepository, returning a Resource<List<SlabConfig>>.
 * */
class GetSlabConfigUseCase @Inject constructor(
    private val repository: SlabConfigRepository
) {
    suspend operator fun invoke(): Resource<List<SlabConfig>> = repository.getSlabConfig()
}
