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

import com.example.reshme_nammapride.domain.model.InstarStage

import com.example.reshme_nammapride.ui.components.ActionCard

import com.example.reshme_nammapride.ui.components.ClimateHeader

import com.example.reshme_nammapride.ui.components.HarvestAlert

import com.example.reshme_nammapride.viewmodel.ClimateViewModel



@OptIn(ExperimentalMaterial3Api::class)

@Composable

fun EntryScreen(viewModel: ClimateViewModel) {

    val temp by viewModel.tempInput.collectAsState()

    val humidity by viewModel.humidityInput.collectAsState()

    val stage by viewModel.selectedStage.collectAsState()

    val advice by viewModel.currentAdvice.collectAsState()

    val activeBatchState by viewModel.activeBatch.collectAsState()



    var newBatchName by remember { mutableStateOf("") }

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

        containerColor = MaterialTheme.colorScheme.background,

        bottomBar = {

            Surface(

                tonalElevation = 3.dp,

                shadowElevation = 8.dp,

                color = if (currentBatch != null) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.background

            ) {

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

                        colors = ButtonDefaults.outlinedButtonColors(

                            containerColor = MaterialTheme.colorScheme.errorContainer,

                            contentColor = MaterialTheme.colorScheme.onErrorContainer

                        ),

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

                        Text("SAVE LOG")

                    }

                }

            }

        }

    ) { paddingValues ->

        if (currentBatch == null) {

            Box(

                modifier = Modifier

                    .fillMaxSize()

                    .padding(paddingValues),

                contentAlignment = Alignment.Center

            ) {

                Card(

                    modifier = Modifier.padding(24.dp),

                    colors = CardDefaults.cardColors(

                        containerColor = MaterialTheme.colorScheme.surfaceVariant,

                        contentColor = MaterialTheme.colorScheme.onSecondaryContainer

                    ),

                    elevation = CardDefaults.cardElevation(2.dp)

                ) {

                    Column(

                        modifier = Modifier.padding(24.dp),

                        horizontalAlignment = Alignment.CenterHorizontally

                    ) {

                        Text(

                            "Start New Rearing",

                            style = MaterialTheme.typography.headlineSmall,

                            fontWeight = FontWeight.Bold,

                            color = MaterialTheme.colorScheme.onSurface

                        )



                        Spacer(modifier = Modifier.height(24.dp))



                        OutlinedTextField(

                            value = newBatchName,

                            onValueChange = { newBatchName = it },

                            label = { Text(

                                color = MaterialTheme.colorScheme.onPrimaryContainer,

                                text = "Breed Name (e.g., CSR2 x CSR4)"

                            ) },

                            colors = OutlinedTextFieldDefaults.colors(

                                focusedBorderColor = MaterialTheme.colorScheme.onPrimaryContainer,

                                ),

                            modifier = Modifier.fillMaxWidth(),

                            singleLine = true,

                            )



                        Spacer(modifier = Modifier.height(24.dp))



                        Button(

                            onClick = {

                                if (newBatchName.isNotBlank()) {

                                    viewModel.createNewBatch(newBatchName)

                                    newBatchName = ""

                                }

                            },

                            modifier = Modifier

                                .fillMaxWidth()

                                .height(56.dp),

                            enabled = newBatchName.isNotBlank()

                        ) {

                            Text("START BATCH")

                        }

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

                val harvestStatus by viewModel.harvestTimerState.collectAsState()



                if (stage == InstarStage.FIFTH_INSTAR && harvestStatus != null) {

                    HarvestAlert(status = harvestStatus!!)

                }



                ClimateHeader(temp, humidity, advice.status)



                Column(modifier = Modifier.padding(16.dp)) {

                    ActionCard(advice)



                    Spacer(modifier = Modifier.height(24.dp))



                    Text(

                        text = "GROWTH STAGE",

                        style = MaterialTheme.typography.labelSmall,

                        color = MaterialTheme.colorScheme.onSurfaceVariant

                    )



                    SecondaryScrollableTabRow(

                        selectedTabIndex = stage.ordinal,

                        modifier = Modifier.fillMaxWidth(),

                        containerColor = Color.Transparent,

                        divider = {},

                        tabs = {

                            InstarStage.entries.forEach { instar ->

                                Tab(

                                    selected = stage == instar,

                                    onClick = { viewModel.updateStage(instar) },

                                    text = {

                                        Text(

                                            text = instar.displayName,

                                            style = MaterialTheme.typography.labelMedium

                                        )

                                    }

                                )

                            }

                        }

                    )



                    Spacer(modifier = Modifier.height(16.dp))



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

fun SliderSection(

    label: String,

    valueText: String,

    value: Float,

    range: ClosedFloatingPointRange<Float>,

    onValueChange: (Float) -> Unit

) {

    Card(

        modifier = Modifier

            .fillMaxWidth()

            .padding(vertical = 8.dp),

        colors = CardDefaults.cardColors(

            containerColor = MaterialTheme.colorScheme.surfaceVariant

        )

    ) {

        Column(modifier = Modifier.padding(16.dp)) {

            Row(

                modifier = Modifier.fillMaxWidth(),

                horizontalArrangement = Arrangement.SpaceBetween,

                verticalAlignment = Alignment.CenterVertically

            ) {

                Text(

                    text = label,

                    style = MaterialTheme.typography.titleLarge,

                    color = MaterialTheme.colorScheme.onSurfaceVariant

                )

                Text(

                    text = valueText,

                    style = MaterialTheme.typography.titleLarge,

                    color = MaterialTheme.colorScheme.onPrimary

                )

            }

            Spacer(modifier = Modifier.height(8.dp))

            Slider(

                value = value,

                onValueChange = onValueChange,

                valueRange = range,

                colors = SliderDefaults.colors(

                    thumbColor = MaterialTheme.colorScheme.primary,

                    activeTrackColor = MaterialTheme.colorScheme.onTertiary,

                    inactiveTrackColor = MaterialTheme.colorScheme.tertiary

                )

            )

        }

    }

}