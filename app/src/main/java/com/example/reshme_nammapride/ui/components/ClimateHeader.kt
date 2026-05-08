package com.example.reshme_nammapride.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.reshme_nammapride.domain.logic.ClimateStatus

@Composable
fun ClimateHeader(
    temperature: Float,
    humidity: Float,
    status: ClimateStatus,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = when (status) {
            ClimateStatus.SAFE -> MaterialTheme.colorScheme.primary
            ClimateStatus.CAUTION -> MaterialTheme.colorScheme.secondaryContainer
            ClimateStatus.DANGER -> MaterialTheme.colorScheme.error
        },
        label = "HeaderColorAnimation"
    )

    val contentColor = MaterialTheme.colorScheme.onPrimary

    val gradientBrush = Brush.verticalGradient(
        colors = listOf(
            backgroundColor,
            backgroundColor.copy(alpha = 0.7f)
        )
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(brush = gradientBrush)
            .padding(vertical = 32.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CURRENT ENVIRONMENT",
            style = MaterialTheme.typography.labelSmall,
            color = contentColor.copy(alpha = 0.8f)
        )

        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            Text(
                text = "${temperature.toInt()}°C",
                style = MaterialTheme.typography.displayLarge,
                color = contentColor
            )

            Spacer(modifier = Modifier.width(24.dp))

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.padding(bottom = 8.dp)
            ) {
                Text(
                    text = "${humidity.toInt()}%",
                    style = MaterialTheme.typography.headlineMedium,
                    color = contentColor,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "HUMIDITY",
                    style = MaterialTheme.typography.labelSmall,
                    color = contentColor.copy(alpha = 0.8f)
                )
            }
        }
    }
}