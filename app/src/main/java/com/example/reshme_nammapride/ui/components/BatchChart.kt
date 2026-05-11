package com.example.reshme_nammapride.ui.components

import android.graphics.Paint
import android.graphics.Typeface
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.nativeCanvas
import androidx.compose.ui.unit.dp
import com.example.reshme_nammapride.data.local.entity.RearingRecord
import com.example.reshme_nammapride.domain.logic.ClimateEngine
import com.example.reshme_nammapride.domain.model.InstarStage

@Composable
fun TemperatureChart(
    modifier: Modifier = Modifier,
    records: List<RearingRecord>
) {
    BatchChart(
        modifier = modifier,
        records = records,
        lineColor = MaterialTheme.colorScheme.primary,
        title = "TEMPERATURE (°C)",
        minVal = 10f,
        maxVal = 45f,
        unitLabel = "°C",
        getValue = { it.temperature },
        getTarget = { ClimateEngine.getTargetTemperature(it) },
        targetLabel = "IDEAL TEMP"
    )
}

@Composable
fun HumidityChart(
    modifier: Modifier = Modifier,
    records: List<RearingRecord>
) {
    BatchChart(
        modifier = modifier,
        records = records,
        lineColor = MaterialTheme.colorScheme.secondary,
        title = "HUMIDITY (%)",
        minVal = 30f,
        maxVal = 100f,
        unitLabel = "%",
        getValue = { it.humidity },
        getTarget = { ClimateEngine.getTargetHumidity(it) },
        targetLabel = "IDEAL HUMIDITY"
    )
}

@Composable
private fun BatchChart(
    modifier: Modifier,
    records: List<RearingRecord>,
    lineColor: Color,
    title: String,
    minVal: Float,
    maxVal: Float,
    unitLabel: String,
    getValue: (RearingRecord) -> Float,
    getTarget: (InstarStage) -> Float,
    targetLabel: String
) {
    val targetLineColor = MaterialTheme.colorScheme.outline
    val axisColor = MaterialTheme.colorScheme.onSurfaceVariant
    val labelTextColor = MaterialTheme.colorScheme.onSurface.toArgb()
    val titleColor = MaterialTheme.colorScheme.onBackground.toArgb()

    Box(modifier = modifier.padding(start = 80.dp, end = 40.dp, top = 80.dp, bottom = 20.dp)) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            val axisPaint = Paint().apply {
                color = axisColor.toArgb()
                textSize = 30f
            }

            drawLine(color = axisColor, start = Offset(0f, 0f), end = Offset(0f, height), strokeWidth = 3f)
            drawLine(color = axisColor, start = Offset(0f, height), end = Offset(width, height), strokeWidth = 3f)

            drawContext.canvas.nativeCanvas.drawText(title, -40f, -55f, Paint().apply {
                color = titleColor
                textSize = 36f
                typeface = Typeface.DEFAULT_BOLD
            })

            drawContext.canvas.nativeCanvas.drawText("Reading Sequence", width / 2 - 140f, height + 100f, Paint().apply {
                color = axisColor.toArgb()
                textSize = 34f
                typeface = Typeface.DEFAULT_BOLD
            })

            if (records.isNotEmpty()) {
                val stepX = if (records.size > 1) width / (records.size - 1) else 0f
                val actualPath = Path()
                val targetPath = Path()

                records.forEachIndexed { index, record ->
                    val x = index * stepX
                    val actualY = height - ((getValue(record) - minVal) / (maxVal - minVal) * height)
                    val targetY = height - ((getTarget(record.stage) - minVal) / (maxVal - minVal) * height)

                    if (index == 0) {
                        actualPath.moveTo(x, actualY)
                        targetPath.moveTo(x, targetY)
                    } else {
                        actualPath.lineTo(x, actualY)
                        targetPath.lineTo(x, targetY)
                    }

                    drawCircle(color = lineColor, radius = 12f, center = Offset(x, actualY))

                    val textLabel = "${getValue(record).toInt()}${if (unitLabel == "°C") "°" else "%"}"
                    val labelYOffset = if (unitLabel == "°C") -35f else 45f

                    drawContext.canvas.nativeCanvas.drawText(textLabel, x - 20f, actualY + labelYOffset, Paint().apply {
                        color = labelTextColor
                        textSize = 30f
                        typeface = Typeface.DEFAULT_BOLD
                    })

                    drawContext.canvas.nativeCanvas.drawText("#${index + 1}", x - 25f, height + 55f, Paint().apply {
                        color = axisColor.toArgb()
                        textSize = 30f
                    })
                }

                drawPath(path = targetPath, color = targetLineColor, style = Stroke(width = 4f), alpha = 0.4f)

                val firstTargetY = height - ((getTarget(records.first().stage) - minVal) / (maxVal - minVal) * height)
                drawContext.canvas.nativeCanvas.drawText(targetLabel, -40f, firstTargetY + 10f, Paint().apply {
                    color = targetLineColor.toArgb()
                    textSize = 26f
                    textAlign = Paint.Align.RIGHT
                    typeface = Typeface.create(Typeface.DEFAULT, Typeface.ITALIC)
                })

                drawPath(path = actualPath, color = lineColor, style = Stroke(width = if (unitLabel == "°C") 16f else 8f))
            }

            drawContext.canvas.nativeCanvas.drawText("${maxVal.toInt()}$unitLabel", -100f, 25f, axisPaint.apply { textAlign = Paint.Align.RIGHT })
            drawContext.canvas.nativeCanvas.drawText("${minVal.toInt()}$unitLabel", -100f, height, axisPaint.apply { textAlign = Paint.Align.RIGHT })
        }
    }
}

fun Color.toArgb(): Int {
    return (this.alpha * 255.0f + 0.5f).toInt() shl 24 or
            ((this.red * 255.0f + 0.5f).toInt() shl 16) or
            ((this.green * 255.0f + 0.5f).toInt() shl 8) or
            (this.blue * 255.0f + 0.5f).toInt()
}