package com.example.reshme_nammapride.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp

@Composable
fun SimpleBatchChart(
    modifier: Modifier = Modifier,
    recordedTemps: List<Float>,
    idealTemp: Float
) {

    val chartLineColor = MaterialTheme.colorScheme.primary
    val referenceLineColor = MaterialTheme.colorScheme.outline
    val dotColor = MaterialTheme.colorScheme.secondaryContainer

    Box(modifier = modifier.padding(16.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height
            val maxTemp = 45f
            val minTemp = 10f


            val idealY = height - ((idealTemp - minTemp) / (maxTemp - minTemp) * height)
            drawLine(
                color = referenceLineColor,
                start = Offset(0f, idealY),
                end = Offset(width, idealY),
                strokeWidth = 2f,
                alpha = 0.6f
            )


            if (recordedTemps.size > 1) {
                val path = Path()
                val stepX = width / (recordedTemps.size - 1)

                recordedTemps.forEachIndexed { index, temp ->

                    val x = index * stepX
                    val y = height - ((temp - minTemp) / (maxTemp - minTemp) * height)

                    if (index == 0) path.moveTo(x, y) else path.lineTo(x, y)


                    drawCircle(
                        color = dotColor,
                        radius = 8f,
                        center = Offset(x, y)
                    )
                }


                drawPath(
                    path = path,
                    color = chartLineColor,
                    style = Stroke(width = 5f)
                )
            }
        }
    }
}