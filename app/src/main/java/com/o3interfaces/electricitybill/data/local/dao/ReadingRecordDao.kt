package com.o3interfaces.electricitybill.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.o3interfaces.electricitybill.data.local.entity.ReadingRecordEntity

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Room DAO for meter readings. Queries the last 1 or 3 records
 * by service number and inserts new reading records.
 * */
@Dao
interface ReadingRecordDao {

    @Query(
        "SELECT * FROM reading_records WHERE service_number = :serviceNumber ORDER BY timestamp DESC LIMIT 3"
    )
    suspend fun getLastThreeReadings(serviceNumber: String): List<ReadingRecordEntity>

    @Query(
        "SELECT * FROM reading_records WHERE service_number = :serviceNumber ORDER BY timestamp DESC LIMIT 1"
    )
    suspend fun getLastReading(serviceNumber: String): ReadingRecordEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReading(entity: ReadingRecordEntity)
}
