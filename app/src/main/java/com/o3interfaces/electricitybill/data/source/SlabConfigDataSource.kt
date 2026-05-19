package com.o3interfaces.electricitybill.data.source

import android.content.Context
import com.google.gson.Gson
import com.o3interfaces.electricitybill.data.dto.SlabConfigDto
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import timber.log.Timber
import javax.inject.Inject
import javax.inject.Singleton

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Reads and parses assets/slab_config.json using Gson on the IO dispatcher.
 * Acts as the raw data source for slab pricing configuration.
 * */
@Singleton
class SlabConfigDataSource @Inject constructor(
    @ApplicationContext private val context: Context,
    private val gson: Gson
) {
    suspend fun loadSlabConfig(): SlabConfigDto = withContext(Dispatchers.IO) {
        val json = context.assets.open("slab_config.json").bufferedReader().use { it.readText() }
        val config = gson.fromJson(json, SlabConfigDto::class.java)
        Timber.w("Slab config loaded with %d slabs", config.slabs.size)
        config
    }
}
