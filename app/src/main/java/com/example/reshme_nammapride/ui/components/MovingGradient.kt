package com.example.reshme_nammapride.ui.components

import androidx.compose.animation.core.*
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color

@Composable
fun MovingGradient(modifier: Modifier = Modifier) {
    val baseColor = MaterialTheme.colorScheme.background
    val highlightColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.15f)

    val colors = listOf(
        highlightColor,
        baseColor,
        baseColor
    )

    val infiniteTransition = rememberInfiniteTransition(label = "gradient")

    val motionProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "progress"
    )

    Box(
        modifier = modifier
            .fillMaxSize()
            .drawBehind {
                val centerX = size.width * motionProgress
                val centerY = size.height * motionProgress

                val brush = Brush.radialGradient(
                    colors = colors,
                    center = Offset(centerX, centerY),
                    radius = size.width * 1.5f
                )
                drawRect(brush)
            }
    )
}