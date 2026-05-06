package com.example.reshme_nammapride.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.happybirthday.R
import com.example.reshme_nammapride.domain.logic.ClimateAdvice
import com.example.reshme_nammapride.domain.logic.ClimateStatus
import com.example.reshme_nammapride.ui.theme.AlertRed
import com.example.reshme_nammapride.ui.theme.SafeGreen
import com.example.reshme_nammapride.ui.theme.WarningOrange

@Composable
fun ActionCard(
    advice: ClimateAdvice,
    modifier: Modifier = Modifier
) {
    // Determine color and icon based on the status
    val cardColor = when (advice.status) {
        ClimateStatus.SAFE -> SafeGreen
        ClimateStatus.CAUTION -> WarningOrange
        ClimateStatus.DANGER -> AlertRed
    }

    val icon = when (advice.status) {
        ClimateStatus.SAFE -> painterResource(R.drawable.outline_check_circle)
        ClimateStatus.CAUTION -> painterResource(R.drawable.info)
        ClimateStatus.DANGER -> painterResource(R.drawable.warning)
    }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = cardColor.copy(alpha = 0.1f) // Light tinted background
        ),
        border = androidx.compose.foundation.BorderStroke(2.dp, cardColor)
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = icon,
                contentDescription = null,
                tint = cardColor,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = when(advice.status) {
                        ClimateStatus.SAFE -> "Status: Ideal"
                        ClimateStatus.CAUTION -> "Action Required"
                        ClimateStatus.DANGER -> "URGENT ALERT"
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = cardColor,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = advice.message,
                    style = MaterialTheme.typography.bodyLarge,
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }
    }
}