package com.o3interfaces.electricitybill.domain.model

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Domain model for a single slab's contribution to a bill:
 * how many units fell into this slab, the rate, and the sub-total cost.
 * */
data class SlabBreakdown(
    val units: Int,
    val ratePerUnit: Double,
    val cost: Double
)
