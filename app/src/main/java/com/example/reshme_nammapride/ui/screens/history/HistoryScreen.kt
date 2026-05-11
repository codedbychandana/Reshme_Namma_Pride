package com.example.reshme_nammapride.ui.screens.history

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import com.example.reshme_nammapride.R
import com.example.reshme_nammapride.data.local.entity.RearingRecord
import com.example.reshme_nammapride.ui.components.HumidityChart
import com.example.reshme_nammapride.ui.components.TemperatureChart
import com.example.reshme_nammapride.ui.navigation.Screen
import com.example.reshme_nammapride.viewmodel.ClimateViewModel
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(
    viewModel: ClimateViewModel,
    navController: NavHostController
) {
    val history by viewModel.historyRecords.collectAsState(initial = emptyList())
    val activeBatch by viewModel.activeBatch.collectAsState()
    val isViewingArchive = history.isNotEmpty() && activeBatch?.id != history.firstOrNull()?.batchId

    Scaffold { padding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if (isViewingArchive) {
                        IconButton(
                            onClick = {
                                viewModel.selectBatch(null)
                                navController.navigate(Screen.Archive.route) {
                                    popUpTo(Screen.History.route) { inclusive = false }
                                }
                            },
                            modifier = Modifier.padding(end = 8.dp)
                        ) {
                            Icon(
                                painter = painterResource(id = R.drawable.chevron_left),
                                contentDescription = "Back",
                                tint = MaterialTheme.colorScheme.primary
                            )
                        }
                    }

                    Column {
                        Text(
                            text = if (isViewingArchive) "Archive Details" else "Batch History",
                            style = MaterialTheme.typography.headlineMedium,
                            fontWeight = FontWeight.Bold
                        )

                        Text(
                            text = if (isViewingArchive) "Viewing past records" else "Past temperature & humidity logs",
                            style = MaterialTheme.typography.bodyMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                if (!isViewingArchive) {
                    Button(
                        onClick = { navController.navigate(Screen.Archive.route) },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(52.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.secondaryContainer,
                            contentColor = MaterialTheme.colorScheme.onSecondaryContainer
                        )
                    ) {
                        Icon(
                            painter = painterResource(id = R.drawable.history),
                            contentDescription = null,
                            modifier = Modifier.size(32.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = "View previous batches",
                            style = MaterialTheme.typography.labelLarge,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            if (history.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {
                    Text("No records found for this batch.")
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(16.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                ) {
                    item {
                        Text(
                            text = "Growth Progress Curves",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                        )

                        val tempList = history.map { it.temperature }

                        TemperatureChart(
                            records = history,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .padding(vertical = 24.dp)
                        )

                        HumidityChart(
                            records = history,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(400.dp)
                                .padding(vertical = 24.dp)
                        )

                        Spacer(modifier = Modifier.height(40.dp))

                        Text(
                            text = "Detailed Logs",
                            style = MaterialTheme.typography.titleMedium,
                            color = MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )
                    }

                    items(history.asReversed()) { record ->
                        HistoryItem(record = record)
                    }
                }
            }
        }
    }
}

@Composable
fun HistoryItem(record: RearingRecord) {
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    val dateFormat = SimpleDateFormat("MMM dd", Locale.getDefault())

    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
        )
    ) {
        Row(
            modifier = Modifier
                .padding(16.dp)
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = record.stage.displayName,
                    style = MaterialTheme.typography.labelLarge,
                    color = MaterialTheme.colorScheme.onPrimary,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = "${dateFormat.format(Date(record.timestamp))} at ${
                        timeFormat.format(
                            Date(
                                record.timestamp
                            )
                        )
                    }",
                    style = MaterialTheme.typography.bodySmall
                )
            }

            Row(verticalAlignment = Alignment.CenterVertically) {
                Column(horizontalAlignment = Alignment.End) {
                    Text(
                        text = "${record.temperature.toInt()}°C",
                        style = MaterialTheme.typography.titleLarge,
                        fontWeight = FontWeight.Bold,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "${record.humidity.toInt()}% RH",
                        style = MaterialTheme.typography.bodyMedium
                    )
                }
            }
        }
    }
}