package com.example.reshme_nammapride.ui.theme

import android.app.Activity
import android.os.Build
import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.dynamicDarkColorScheme
import androidx.compose.material3.dynamicLightColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext

private val DarkColorScheme = darkColorScheme(
    primary = RawSilkGold,
    secondary = SoftLeafGreen,
    tertiary = SilkCream,
    background = DeepCharcoal,
    surface = DeepCharcoal,
    onPrimary = Black,
    onBackground = White,
    onSurface = White
)

private val LightColorScheme = lightColorScheme(
    primary = MulberryGreen,
    secondary = SoftLeafGreen,
    tertiary = RawSilkGold,
    background = SilkCream,
    surface = White,
    onPrimary = White,
    onBackground = DeepCharcoal,
    onSurface = DeepCharcoal
)

@Composable
fun ReshmeNammaPrideTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    // Dynamic color is available on Android 12+
    content: @Composable () -> Unit
) {
    val colorScheme = when {
        darkTheme -> DarkColorScheme
        else -> LightColorScheme
    }

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}