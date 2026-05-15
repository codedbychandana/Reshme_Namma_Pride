package com.example.reshme_nammapride.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.reshme_nammapride.data.local.dao.RearingDao
import com.example.reshme_nammapride.data.local.entity.Batch
import com.example.reshme_nammapride.data.local.entity.RearingRecord
import com.example.reshme_nammapride.domain.logic.ClimateAdvice
import com.example.reshme_nammapride.domain.logic.ClimateEngine
import com.example.reshme_nammapride.domain.model.InstarStage
import com.example.reshme_nammapride.util.HarvestTimer
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch

// view model for batch tracking related operations
class ClimateViewModel(private val dao: RearingDao) : ViewModel() {

    val activeBatch = dao.getActiveBatch().stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = null
    )

    val pastBatches: StateFlow<List<Batch>> = dao.getPastBatches()
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _selectedBatchId = MutableStateFlow<Int?>(null)

    @OptIn(ExperimentalCoroutinesApi::class)
    val historyRecords: StateFlow<List<RearingRecord>> = combine(
        activeBatch,
        _selectedBatchId
    ) { active, selected ->
        selected ?: active?.id
    }.flatMapLatest { id ->
        if (id != null) {
            dao.getRecordsForBatch(id)
        } else {
            flowOf(emptyList())
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    private val _tempInput = MutableStateFlow(25f)
    val tempInput = _tempInput.asStateFlow()

    private val _humidityInput = MutableStateFlow(75f)
    val humidityInput = _humidityInput.asStateFlow()

    private val _selectedStage = MutableStateFlow(InstarStage.FIRST_INSTAR)
    val selectedStage = _selectedStage.asStateFlow()

    val currentAdvice: StateFlow<ClimateAdvice> = combine(
        _selectedStage, _tempInput, _humidityInput
    ) { stage, temp, hum ->
        ClimateEngine.analyze(stage, temp, hum)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = ClimateEngine.analyze(InstarStage.FIRST_INSTAR, 25f, 75f)
    )

    val harvestTimerState = historyRecords.map { records ->
        HarvestTimer.calculateStatus(records)
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    fun selectBatch(batchId: Int?) {
        _selectedBatchId.value = batchId
    }

    fun updateTemperature(newTemp: Float) { _tempInput.value = newTemp }
    fun updateHumidity(newHum: Float) { _humidityInput.value = newHum }
    fun updateStage(newStage: InstarStage) { _selectedStage.value = newStage }

    fun saveReading(batchId: Int) {
        viewModelScope.launch {
            val record = RearingRecord(
                batchId = batchId,
                timestamp = System.currentTimeMillis(),
                temperature = _tempInput.value,
                humidity = _humidityInput.value,
                stage = _selectedStage.value
            )
            dao.insertRecord(record)
        }
    }

    fun createNewBatch(name: String) {
        viewModelScope.launch {
            val newBatch = Batch(
                breedName = name,
                startDate = System.currentTimeMillis(),
                isActive = true
            )
            dao.upsertBatch(newBatch)
        }
    }

    fun completeActiveBatch() {
        viewModelScope.launch {
            activeBatch.value?.let { current ->
                dao.upsertBatch(current.copy(isActive = false))
                selectBatch(null)
            }
        }
    }

    // csv export system
    fun exportBatchData(context: android.content.Context, uri: android.net.Uri, batch: Batch) {
        viewModelScope.launch(Dispatchers.IO) {
            try {
                val readings = dao.getRecordsForBatch(batch.id).first().sortedBy { it.timestamp }
                val dateFormat = java.text.SimpleDateFormat("yyyy-MM-dd HH:mm:ss", java.util.Locale.getDefault())

                val csvHeader = "Date/Time,Temperature(°C),Humidity(%),Growth Stage\n"
                val csvBody = readings.joinToString("\n") { reading ->
                    val dateStr = dateFormat.format(java.util.Date(reading.timestamp))
                    "$dateStr,${reading.temperature},${reading.humidity},${reading.stage}"
                }

                context.contentResolver.openOutputStream(uri)?.use { outputStream ->
                    outputStream.write((csvHeader + csvBody).toByteArray(Charsets.UTF_8))
                    outputStream.flush()
                }
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}