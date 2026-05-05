package com.example.reshme_nammapride.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.example.reshme_nammapride.domain.model.InstarStage

@Entity(tableName = "rearing_records")
data class RearingRecord(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val batchId: Int, // Foreign Key link to Batch
    val timestamp: Long,
    val temperature: Float,
    val humidity: Float,
    val stage: InstarStage
)