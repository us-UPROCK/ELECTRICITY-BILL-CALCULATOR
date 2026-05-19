package com.o3interfaces.electricitybill.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Room entity mapping the reading_records table. Stores service number,
 * meter reading value, calculated cost, and timestamp. The service_number
 * column is indexed for fast per-customer queries.
 * */
@Entity(
    tableName = "reading_records",
    indices = [Index(value = ["service_number"])]
)
data class ReadingRecordEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    @ColumnInfo(name = "service_number")
    val serviceNumber: String,
    val reading: Int,
    val cost: Double,
    val timestamp: Long
)
