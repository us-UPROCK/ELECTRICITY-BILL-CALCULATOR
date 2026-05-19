package com.o3interfaces.electricitybill.di

import com.o3interfaces.electricitybill.data.repository.MeterReadingRepositoryImpl
import com.o3interfaces.electricitybill.data.repository.SlabConfigRepositoryImpl
import com.o3interfaces.electricitybill.domain.repository.MeterReadingRepository
import com.o3interfaces.electricitybill.domain.repository.SlabConfigRepository
import dagger.Binds
import dagger.Module
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Hilt module that binds repository interfaces to their
 * concrete implementations using @Binds.
 * */
@Module
@InstallIn(SingletonComponent::class)
abstract class RepositoryModule {

    @Binds
    @Singleton
    abstract fun bindMeterReadingRepository(
        impl: MeterReadingRepositoryImpl
    ): MeterReadingRepository

    @Binds
    @Singleton
    abstract fun bindSlabConfigRepository(
        impl: SlabConfigRepositoryImpl
    ): SlabConfigRepository
}
