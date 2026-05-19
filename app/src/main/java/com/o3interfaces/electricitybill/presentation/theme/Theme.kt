package com.o3interfaces.electricitybill.presentation.theme

import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

/**
 * Created by U5M4N
 * It's the fear of what's coming after the doing
 * that makes doing more harder to do
 * */

/**
 * Material3 theme composable wiring together light/dark color schemes and typography.
 * Dynamic color is disabled so the electric blue branding always applies.
 * */
private val DarkColorScheme = darkColorScheme(
    primary = ElectricBlue80,
    secondary = ElectricBlueGrey80,
    tertiary = Amber80
)

private val LightColorScheme = lightColorScheme(
    primary = ElectricBlue40,
    secondary = ElectricBlueGrey40,
    tertiary = Amber40
)

@Composable
fun ElectricityBillTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    dynamicColor: Boolean = false,
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        dynamicColor && Build.VERSION.SDK_INT >= Build.VERSION_CODES.S -> {
            val context = LocalContext.current
            if (darkTheme) dynamicDarkColorScheme(context) else dynamicLightColorScheme(context)
        }
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}
