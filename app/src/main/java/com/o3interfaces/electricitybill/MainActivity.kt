package com.o3interfaces.electricitybill

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import com.o3interfaces.electricitybill.presentation.screen.MeterReadingScreen
import com.o3interfaces.electricitybill.presentation.theme.ElectricityBillTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Single Activity host. Enables edge-to-edge display and
 * launches MeterReadingScreen inside the app theme.
 * */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ElectricityBillTheme {
                MeterReadingScreen()
            }
        }
    }
}
