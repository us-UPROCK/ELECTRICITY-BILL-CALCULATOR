package com.o3interfaces.electricitybill.di

import android.content.Context
import androidx.room.Room
import com.o3interfaces.electricitybill.data.local.dao.ReadingRecordDao
import com.o3interfaces.electricitybill.data.local.db.AppDatabase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Hilt module that builds the Room database singleton
 * and exposes ReadingRecordDao for injection.
 * */
@Module
@InstallIn(SingletonComponent::class)
object DatabaseModule {

    @Provides
    @Singleton
    fun provideAppDatabase(@ApplicationContext context: Context): AppDatabase =
        Room.databaseBuilder(
            context,
            AppDatabase::class.java,
            "electricity_bill.db"
        ).build()

    @Provides
    fun provideReadingRecordDao(db: AppDatabase): ReadingRecordDao = db.readingRecordDao()
}
