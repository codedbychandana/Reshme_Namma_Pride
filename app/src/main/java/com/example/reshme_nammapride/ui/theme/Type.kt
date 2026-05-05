package com.example.reshme_nammapride.ui.theme

import androidx.compose.material3.Typography
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.Font
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.happybirthday.R

// Set of Material typography styles to start with
val HelveticaFamily = FontFamily(
    Font(R.font.helvetica, FontWeight.Normal),
    Font(R.font.helvetica_bold, FontWeight.Bold),
    Font(R.font.helvetica_light, FontWeight.Light)
)

val Typography = Typography(
    // Used for the "Climate Dial" temperature display
    displayLarge = TextStyle(
        fontFamily = HelveticaFamily,
        fontWeight = FontWeight.Bold,
        fontSize = 57.sp,
        lineHeight = 64.sp,
        letterSpacing = (-0.25).sp
    ),

    // Used for "Smart Advice" headers
    headlineMedium = TextStyle(
        fontFamily = HelveticaFamily,
        fontWeight = FontWeight.SemiBold,
        fontSize = 28.sp,
        lineHeight = 36.sp
    ),

    // Used for Main Labels (e.g., "Current Instar: Stage 3")
    titleLarge = TextStyle(
        fontFamily = HelveticaFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 22.sp,
        lineHeight = 28.sp
    ),

    // Used for Actionable Alerts and Instructions
    bodyLarge = TextStyle(
        fontFamily = HelveticaFamily,
        fontWeight = FontWeight.Normal,
        fontSize = 16.sp,
        lineHeight = 24.sp,
        letterSpacing = 0.5.sp
    ),

    // Used for small metadata (e.g., "Last logged: 2:00 PM")
    labelMedium = TextStyle(
        fontFamily = HelveticaFamily,
        fontWeight = FontWeight.Medium,
        fontSize = 12.sp,
        lineHeight = 16.sp,
        letterSpacing = 0.5.sp
    )
)