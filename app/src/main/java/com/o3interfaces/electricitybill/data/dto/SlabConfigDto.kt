package com.o3interfaces.electricitybill.data.dto

import com.google.gson.annotations.SerializedName

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Gson DTOs for deserializing slab_config.json. SlabDto represents
 * a single pricing slab; SlabConfigDto is the root wrapper object.
 * */
data class SlabDto(
    @SerializedName("upToUnits") val upToUnits: Int?,
    @SerializedName("ratePerUnit") val ratePerUnit: Double
)

data class SlabConfigDto(
    @SerializedName("slabs") val slabs: List<SlabDto>
)
