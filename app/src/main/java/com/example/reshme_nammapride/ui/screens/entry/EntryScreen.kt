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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.example.reshme_nammapride.R
import com.example.reshme_nammapride.domain.model.InstarStage
import com.example.reshme_nammapride.ui.components.*
import com.example.reshme_nammapride.viewmodel.ClimateViewModel
import com.example.reshme_nammapride.viewmodel.ManagementViewModel
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EntryScreen(
    climateViewModel: ClimateViewModel,
    managementViewModel: ManagementViewModel
) {
    val temp by climateViewModel.tempInput.collectAsState()
    val humidity by climateViewModel.humidityInput.collectAsState()
    val stage by climateViewModel.selectedStage.collectAsState()
    val advice by climateViewModel.currentAdvice.collectAsState()
    val activeBatchState by climateViewModel.activeBatch.collectAsState()

    val mgtAdviceId by managementViewModel.managementAdviceResId.collectAsState()
    val mgtIconId by managementViewModel.managementIconResId.collectAsState()

    var newBatchName by remember { mutableStateOf("") }
    var showFinishDialog by remember { mutableStateOf(false) }
    val currentBatch = activeBatchState

    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    if (showFinishDialog && currentBatch != null) {
        AlertDialog(
            onDismissRequest = { showFinishDialog = false },
            title = { Text(stringResource(R.string.dialog_finish_title)) },
            text = {
                Text(stringResource(R.string.dialog_finish_desc, currentBatch.breedName))
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        climateViewModel.completeActiveBatch()
                        showFinishDialog = false
                    }
                ) {
                    Text(stringResource(R.string.btn_finish), color = MaterialTheme.colorScheme.error)
                }
            },
            dismissButton = {
                TextButton(onClick = { showFinishDialog = false }) {
                    Text(stringResource(R.string.btn_cancel))
                }
            }
        )
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.app_name),
                        fontWeight = FontWeight.Bold
                    )
                },
                actions = {
                    LanguageToggle()
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.surface
                )
            )
        },
        snackbarHost = { AppSnackbar(hostState = snackbarHostState) },
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
                        Text(stringResource(R.string.btn_finish))
                    }

                    Button(
                        onClick = {
                            currentBatch?.let {
                                climateViewModel.saveReading(it.id)
                                scope.launch {
                                    snackbarHostState.showSnackbar(
                                        "Saved record successfully"
                                    )
                                }
                            }
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
                        Text(stringResource(R.string.btn_save_log))
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
                            text = stringResource(R.string.title_start_new),
                            style = MaterialTheme.typography.headlineSmall,
                            fontWeight = FontWeight.Bold,
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(24.dp))

                        OutlinedTextField(
                            value = newBatchName,
                            onValueChange = { newBatchName = it },
                            label = { Text(text = stringResource(R.string.label_breed_name)) },
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
                                    climateViewModel.createNewBatch(newBatchName)
                                    newBatchName = ""
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(56.dp),
                            enabled = newBatchName.isNotBlank()
                        ) {
                            Text(stringResource(R.string.btn_start_batch))
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
                val harvestStatus by climateViewModel.harvestTimerState.collectAsState()

                if (stage == InstarStage.FIFTH_INSTAR && harvestStatus != null) {
                    HarvestAlert(status = harvestStatus!!)
                }

                ClimateHeader(temp, humidity, advice.status)

                Column(modifier = Modifier.padding(16.dp)) {
                    ActionCard(advice)

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = stringResource(R.string.label_growth_stage),
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
                                    onClick = {
                                        climateViewModel.updateStage(instar)
                                        managementViewModel.updateStage(instar)
                                    },
                                    text = {
                                        Text(
                                            text = stringResource(instar.displayNameResId),
                                            style = MaterialTheme.typography.labelMedium
                                        )
                                    }
                                )
                            }
                        }
                    )

                    Spacer(modifier = Modifier.height(16.dp))

                    ManagementCard(advice = mgtAdviceId, iconResId = mgtIconId)

                    SliderSection(
                        label = stringResource(R.string.label_temp),
                        valueText = "${temp.toInt()}°C",
                        value = temp,
                        range = 10f..45f,
                        onValueChange = { climateViewModel.updateTemperature(it) }
                    )

                    SliderSection(
                        label = stringResource(R.string.label_humidity),
                        valueText = "${humidity.toInt()}%",
                        value = humidity,
                        range = 20f..100f,
                        onValueChange = { climateViewModel.updateHumidity(it) }
                    )
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