package com.example.reshme_nammapride.ui.theme

import androidx.compose.foundation.isSystemInDarkTheme
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.darkColorScheme
import androidx.compose.material3.lightColorScheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color

private val LightColorScheme = lightColorScheme(
    primary = MulberryGreen40,
    onPrimary = Color.White,
    primaryContainer = MulberryGreen90,
    onPrimaryContainer = MulberryGreen40,

    tertiary = Neutral90,
    onTertiary = MulberryGreen30,

    background = MulberryGreen90,
    onBackground = Neutral10,
    surface = Neutral99,
    onSurface = Neutral10,

    surfaceVariant = MulberryGreen60,
    onSurfaceVariant = NeutralVariant30,
    outline = NeutralVariant50,
    outlineVariant = WarningOrangeOutline,

    error = Error40,
    onError = Color.White,
    errorContainer = Error90,
    onErrorContainer = Error10,

    secondaryContainer = WarningOrangeContainer,
    onSecondaryContainer = WarningOrangeText
)

private val DarkColorScheme = darkColorScheme(
    onPrimary = MulberryGreen90,
    primary = MulberryGreen40,
    onPrimaryContainer = MulberryGreen90,
    primaryContainer = MulberryGreen40,

    tertiary = Neutral90,
    onTertiary = Neutral99,

    background = Neutral10,
    surface = Neutral10,
    onBackground = Neutral99,
    onSurface = Error90,
    onSurfaceVariant = MulberryGreen90,
    surfaceVariant = NeutralVariant30,
    outline = NeutralVariant50,
    outlineVariant = WarningOrangeOutline,

    error = Error40,
    onError = Color.White,
    onErrorContainer = Error90,
    errorContainer = Error10,

    secondaryContainer = WarningOrangeContainerDarkTheme,
    onSecondaryContainer = WarningOrangeText

)

@Composable
fun ReshmeNammaPrideTheme(
    darkTheme: Boolean = isSystemInDarkTheme(),
    content: @Composable () -> Unit
) {
    val colorScheme = if (darkTheme) DarkColorScheme else LightColorScheme

    MaterialTheme(
        colorScheme = colorScheme,
        typography = Typography,
        content = content
    )
}