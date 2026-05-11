package com.example.reshme_nammapride.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource // Added import
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.reshme_nammapride.R
import com.example.reshme_nammapride.domain.logic.ClimateAdvice
import com.example.reshme_nammapride.domain.logic.ClimateStatus

@Composable
fun ActionCard(
    advice: ClimateAdvice,
    modifier: Modifier = Modifier
) {
    val containerColor = when (advice.status) {
        ClimateStatus.SAFE -> MaterialTheme.colorScheme.primaryContainer
        ClimateStatus.CAUTION -> MaterialTheme.colorScheme.secondaryContainer
        ClimateStatus.DANGER -> MaterialTheme.colorScheme.errorContainer
    }

    val contentColor = when (advice.status) {
        ClimateStatus.SAFE -> MaterialTheme.colorScheme.onPrimaryContainer
        ClimateStatus.CAUTION -> MaterialTheme.colorScheme.onSecondaryContainer
        ClimateStatus.DANGER -> MaterialTheme.colorScheme.onErrorContainer
    }

    val outlineColor = when (advice.status) {
        ClimateStatus.SAFE -> MaterialTheme.colorScheme.primary
        ClimateStatus.CAUTION -> MaterialTheme.colorScheme.outlineVariant
        ClimateStatus.DANGER -> MaterialTheme.colorScheme.error
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
            containerColor = containerColor,
            contentColor = contentColor
        ),
        border = androidx.compose.foundation.BorderStroke(2.dp, outlineColor)
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
                tint = contentColor,
                modifier = Modifier.size(48.dp)
            )

            Spacer(modifier = Modifier.width(16.dp))

            Column {
                Text(
                    text = when(advice.status) {
                        ClimateStatus.SAFE -> stringResource(R.string.status_label_ideal)
                        ClimateStatus.CAUTION -> stringResource(R.string.status_label_action)
                        ClimateStatus.DANGER -> stringResource(R.string.status_label_urgent)
                    },
                    style = MaterialTheme.typography.titleLarge,
                    color = contentColor,
                    fontWeight = FontWeight.Bold
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(id = advice.messageResId),
                    style = MaterialTheme.typography.bodyLarge,
                    color = contentColor
                )
            }
        }
    }
}