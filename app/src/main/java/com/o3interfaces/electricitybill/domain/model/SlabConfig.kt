package com.o3interfaces.electricitybill.domain.model

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Domain model for a single pricing slab. upToUnits is the inclusive
 * upper bound for this slab (null means unlimited / final tier).
 * */
data class SlabConfig(
    val upToUnits: Int?,
    val ratePerUnit: Double
)
