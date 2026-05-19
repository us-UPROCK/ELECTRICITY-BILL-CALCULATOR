package com.o3interfaces.electricitybill.data.local.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.o3interfaces.electricitybill.data.local.dao.ReadingRecordDao
import com.o3interfaces.electricitybill.data.local.entity.ReadingRecordEntity

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Room database definition. Registers ReadingRecordEntity
 * and exposes ReadingRecordDao for data access.
 * */
@Database(entities = [ReadingRecordEntity::class], version = 1, exportSchema = false)
abstract class AppDatabase : RoomDatabase() {
    abstract fun readingRecordDao(): ReadingRecordDao
}
