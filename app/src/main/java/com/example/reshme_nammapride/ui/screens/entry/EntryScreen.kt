package com.example.reshme_nammapride.ui.screens.entry

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
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

    Scaffold(
        bottomBar = {

            val currentBatch = activeBatchState

            Button(
                onClick = {
                    if (currentBatch != null) {
                        viewModel.saveReading(currentBatch.id)
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .height(56.dp),
                enabled = currentBatch != null
            ) {
                Text("SAVE LOG")
            }
        }
    ) { paddingValues ->
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

                // Input Sliders
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