package com.example.reshme_nammapride.ui.screens.entry

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.reshme_nammapride.domain.model.InstarStage
import com.example.reshme_nammapride.ui.components.ActionCard
import com.example.reshme_nammapride.ui.components.ClimateHeader
import com.example.reshme_nammapride.viewmodel.ClimateViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(viewModel: ClimateViewModel) {
    val temp by viewModel.tempInput.collectAsState()
    val humidity by viewModel.humidityInput.collectAsState()
    val stage by viewModel.selectedStage.collectAsState()
    val advice by viewModel.currentAdvice.collectAsState()
    val activeBatchState by viewModel.activeBatch.collectAsState()


    var showFinishDialog by remember { mutableStateOf(false) }
    val currentBatch = activeBatchState


    if (showFinishDialog && currentBatch != null) {
        AlertDialog(
            onDismissRequest = { showFinishDialog = false },
            title = { Text("Finish Batch?") },
            text = { Text("Are you sure you want to finish '${currentBatch.breedName}'? You cannot add more logs once closed.") },
            confirmButton = {
                TextButton(
                    onClick = {
                        viewModel.completeActiveBatch()
                        showFinishDialog = false
                    }
                ) {
                    Text("FINISH", color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showFinishDialog = false }) {
                    Text("CANCEL")
                }
            }
        )
    }

    Scaffold(
        bottomBar = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedButton(
                    onClick = { showFinishDialog = true },
                    modifier = Modifier
                        .weight(0.4f)
                        .height(56.dp),
                    enabled = currentBatch != null,
                    colors = ButtonDefaults.outlinedButtonColors(contentColor = MaterialTheme.colorScheme.error),
                    border = BorderStroke(1.dp, MaterialTheme.colorScheme.error)
                ) {
                    Text("FINISH")
                }

                Button(
                    onClick = {
                        currentBatch?.let { viewModel.saveReading(it.id) }
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.primary,
                        contentColor = MaterialTheme.colorScheme.onPrimary
                    ),
                    modifier = Modifier
                        .weight(0.6f)
                        .height(56.dp),
                    enabled = currentBatch != null
                ) {
                    Text("SAVE LOG", color = MaterialTheme.colorScheme.onPrimary)
                }
            }
        }
    ) { paddingValues ->
        if (currentBatch == null) {
            Box(
                modifier = Modifier.fillMaxSize().padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Text("No Active Batch", style = MaterialTheme.typography.headlineSmall)
                    Button(
                        onClick = { viewModel.createNewBatch("New Batch") },
                        modifier = Modifier.padding(top = 16.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = MaterialTheme.colorScheme.primary,
                            contentColor = MaterialTheme.colorScheme.onPrimary
                        )
                    ) {
                        Text("START REARING")
                    }
                }
            }
        } else {
            Column(
                modifier = Modifier
                    .padding(paddingValues)
                    .fillMaxSize()
                    .verticalScroll(rememberScrollState())
            ) {
                ClimateHeader(temp, humidity, advice.status)

                Column(modifier = Modifier.padding(16.dp)) {
                    ActionCard(advice)

                    Spacer(modifier = Modifier.height(24.dp))

                    SecondaryScrollableTabRow(
                        selectedTabIndex = stage.ordinal,
                        modifier = Modifier.fillMaxWidth(),
                        containerColor = Color.Transparent,
                        tabs = {
                            InstarStage.entries.forEach { instar ->
                                Tab(
                                    selected = stage == instar,
                                    onClick = { viewModel.updateStage(instar) },
                                    text = { Text(instar.displayName) }
                                )
                            }
                        }
                    )

                    SliderSection("Temperature", "${temp.toInt()}°C", temp, 10f..45f) {
                        viewModel.updateTemperature(it)
                    }

                    SliderSection("Humidity", "${humidity.toInt()}%", humidity, 20f..100f) {
                        viewModel.updateHumidity(it)
                    }
                }
            }
        }
    }
}

@Composable
fun SliderSection(label: String, valueText: String, value: Float, range: ClosedFloatingPointRange<Float>, onValueChange: (Float) -> Unit) {
    Card(modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(Modifier.fillMaxWidth(), Arrangement.SpaceBetween) {
                Text(label, fontWeight = FontWeight.Bold)
                Text(valueText, color = MaterialTheme.colorScheme.primary)
            }
            Slider(value = value, onValueChange = onValueChange, valueRange = range)
        }
    }
}