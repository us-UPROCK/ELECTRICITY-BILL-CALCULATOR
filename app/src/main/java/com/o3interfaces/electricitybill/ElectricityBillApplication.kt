package com.o3interfaces.electricitybill

import android.app.Application
import dagger.hilt.android.HiltAndroidApp
import timber.log.Timber

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Application entry point. Initializes Hilt DI and plants
 * Timber DebugTree for log output in debug builds.
 * */
@HiltAndroidApp
class ElectricityBillApplication : Application() {
    override fun onCreate() {
        super.onCreate()
        if (BuildConfig.DEBUG) {
            Timber.plant(Timber.DebugTree())
        }
    }
}
