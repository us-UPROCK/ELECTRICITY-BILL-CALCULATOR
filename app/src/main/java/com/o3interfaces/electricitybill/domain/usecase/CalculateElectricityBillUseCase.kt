package com.o3interfaces.electricitybill.domain.usecase

import com.o3interfaces.electricitybill.domain.model.BillCalculation
import com.o3interfaces.electricitybill.domain.model.SlabBreakdown
import com.o3interfaces.electricitybill.domain.model.SlabConfig
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
 * Core billing algorithm. Applies a progressive O(n) slab calculation:
 * distributes consumption units across ordered pricing slabs and returns
 * a full BillCalculation with per-slab breakdown and total cost.
 * */
class CalculateElectricityBillUseCase @Inject constructor() {

    suspend operator fun invoke(consumption: Int, slabs: List<SlabConfig>): BillCalculation =
        withContext(Dispatchers.Default) {
            Timber.d("Calculating for %d units, %d slabs", consumption, slabs.size)

            if (consumption == 0) {
                return@withContext BillCalculation(0, emptyList(), 0.0)
            }

            val breakdown = mutableListOf<SlabBreakdown>()
            var previousUpperBound = 0
            var remainingUnits = consumption

            for (slab in slabs) {
                if (remainingUnits <= 0) break

                val slabCapacity = if (slab.upToUnits != null) {
                    slab.upToUnits - previousUpperBound
                } else {
                    remainingUnits
                }

                val unitsInSlab = minOf(remainingUnits, slabCapacity)
                val cost = unitsInSlab * slab.ratePerUnit

                breakdown.add(SlabBreakdown(unitsInSlab, slab.ratePerUnit, cost))
                remainingUnits -= unitsInSlab
                previousUpperBound = slab.upToUnits ?: previousUpperBound
            }

            BillCalculation(
                consumption = consumption,
                breakdown = breakdown,
                totalCost = breakdown.sumOf { it.cost }
            )
        }
}
