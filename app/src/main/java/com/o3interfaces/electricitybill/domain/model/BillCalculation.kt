package com.o3interfaces.electricitybill.domain.model

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Domain model holding the complete bill result: total consumption units,
 * a per-slab breakdown list, and the grand total cost.
 * */
data class BillCalculation(
    val consumption: Int,
    val breakdown: List<SlabBreakdown>,
    val totalCost: Double
)
