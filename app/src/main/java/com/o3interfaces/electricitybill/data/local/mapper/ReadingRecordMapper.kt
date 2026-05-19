package com.o3interfaces.electricitybill.data.local.mapper

import com.o3interfaces.electricitybill.data.local.entity.ReadingRecordEntity
import com.o3interfaces.electricitybill.domain.model.ReadingRecord

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Extension functions that convert between ReadingRecordEntity (data layer)
 * and ReadingRecord (domain layer) in both directions.
 * */
fun ReadingRecordEntity.toDomain(): ReadingRecord = ReadingRecord(
    id = id,
    serviceNumber = serviceNumber,
    reading = reading,
    cost = cost,
    timestamp = timestamp
)

fun ReadingRecord.toEntity(): ReadingRecordEntity = ReadingRecordEntity(
    id = id,
    serviceNumber = serviceNumber,
    reading = reading,
    cost = cost,
    timestamp = timestamp
)
