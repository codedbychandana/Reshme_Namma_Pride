package com.example.reshme_nammapride.ui.components

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reshme_nammapride.domain.logic.ClimateStatus
import com.example.reshme_nammapride.ui.theme.AlertRed
import com.example.reshme_nammapride.ui.theme.SafeGreen
import com.example.reshme_nammapride.ui.theme.WarningOrange

@Composable
fun ClimateHeader(
    temperature: Float,
    humidity: Float,
    status: ClimateStatus,
    modifier: Modifier = Modifier
) {
    val backgroundColor by animateColorAsState(
        targetValue = when (status) {
            ClimateStatus.SAFE -> SafeGreen
            ClimateStatus.CAUTION -> WarningOrange
            ClimateStatus.DANGER -> AlertRed
        }
    )

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(backgroundColor)
            .padding(vertical = 32.dp, horizontal = 16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "CURRENT ENVIRONMENT",
            style = MaterialTheme.typography.labelMedium,
            color = Color.White.copy(alpha = 0.8f)
        )

        Row(
            verticalAlignment = Alignment.Bottom,
            modifier = Modifier.padding(top = 8.dp)
        ) {
            // Large Temp Display
            Text(
                text = "${temperature.toInt()}°C",
                style = MaterialTheme.typography.displayLarge.copy(
                    fontSize = 72.sp,
                    color = Color.White,
                    fontWeight = FontWeight.Black
                )
            )

            Spacer(modifier = Modifier.width(24.dp))

            // Humidity Display
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = "${humidity.toInt()}%",
                    style = MaterialTheme.typography.headlineMedium.copy(
                        color = Color.White,
                        fontWeight = FontWeight.Bold
                    )
                )
                Text(
                    text = "HUMIDITY",
                    style = MaterialTheme.typography.labelMedium,
                    color = Color.White.copy(alpha = 0.8f)
                )
            }
        }
    }
}