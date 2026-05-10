package com.example.reshme_nammapride.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp

@Composable
fun BatchChart(
    modifier: Modifier = Modifier,
    recordedTemps: List<Float>,
    idealTemp: Float
) {
    val chartLineColor = MaterialTheme.colorScheme.primary
    val referenceLineColor = MaterialTheme.colorScheme.outline
    val dotColor = MaterialTheme.colorScheme.secondaryContainer
    val labelTextColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val axisColor = MaterialTheme.colorScheme.onSurfaceVariant.toArgb()

    Box(modifier = modifier.padding(start = 70.dp, end = 30.dp, top = 60.dp, bottom = 80.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val maxTemp = 50f
            val minTemp = 5f

            val idealY = height - ((idealTemp - minTemp) / (maxTemp - minTemp) * height)

            drawLine(
                color = referenceLineColor,
                start = Offset(0f, 0f),
                end = Offset(0f, height),
                strokeWidth = 4f
            )
            drawLine(
                color = referenceLineColor,
                start = Offset(0f, height),
                end = Offset(width, height),
                strokeWidth = 4f
            )

            drawContext.canvas.nativeCanvas.drawText(
                "Temperature (°C)",
                -60f,
                -40f,
                Paint().apply {
                    color = axisColor
                    textSize = 36f
                    typeface = Typeface.DEFAULT_BOLD
                }
            )

            drawContext.canvas.nativeCanvas.drawText(
                "Reading Sequence",
                width / 2 - 120f,
                height + 90f,
                Paint().apply {
                    color = axisColor
                    textSize = 36f
                    typeface = Typeface.DEFAULT_BOLD
                }
            )

            drawLine(
                color = referenceLineColor,
                start = Offset(0f, idealY),
                end = Offset(width, idealY),
                strokeWidth = 4f,
                alpha = 0.4f
            )

            drawContext.canvas.nativeCanvas.drawText(
                "TARGET: ${idealTemp.toInt()}°C",
                width - 250f,
                idealY - 25f,
                Paint().apply {
                    color = labelTextColor
                    textSize = 38f
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.BOLD_ITALIC)
                }
            )

            if (recordedTemps.isNotEmpty()) {
                val path = Path()
                val stepX = if (recordedTemps.size > 1) width / (recordedTemps.size - 1) else 0f

                recordedTemps.forEachIndexed { index, temp ->
                    val x = index * stepX
                    val y = height - ((temp - minTemp) / (maxTemp - minTemp) * height)

                    if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)

                    drawCircle(
                        color = dotColor,
                        radius = 20f,
                        center = Offset(x, y)
                    )

                    val textOffset = if (y < idealY) -50f else 70f
                    drawContext.canvas.nativeCanvas.drawText(
                        "${temp.toInt()}°",
                        x - 25f,
                        y + textOffset,
                        Paint().apply {
                            color = labelTextColor
                            textSize = 42f
                            typeface = Typeface.DEFAULT_BOLD
                        }
                    )

                    drawContext.canvas.nativeCanvas.drawText(
                        "#${index + 1}",
                        x - 25f,
                        height + 50f,
                        Paint().apply {
                            color = axisColor
                            textSize = 32f
                        }
                    )
                }

                drawPath(
                    path = path,
                    color = chartLineColor,
                    style = Stroke(width = 20f)
                )
            }

            drawContext.canvas.nativeCanvas.drawText(
                "50°C",
                -85f,
                20f,
                Paint().apply { color = axisColor; textSize = 32f }
            )
            drawContext.canvas.nativeCanvas.drawText(
                "5°C",
                -85f,
                height,
                Paint().apply { color = axisColor; textSize = 32f }
            )
        }
    }
}

fun androidx.compose.ui.graphics.Color.toArgb(): Int {
    return (this.alpha * 255.0f + 0.5f).toInt() shl 24 or
            ((this.red * 255.0f + 0.5f).toInt() shl 16) or
            ((this.green * 255.0f + 0.5f).toInt() shl 8) or
            (this.blue * 255.0f + 0.5f).toInt()
}