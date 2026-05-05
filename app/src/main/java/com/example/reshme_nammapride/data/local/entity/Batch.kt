package com.example.reshme_nammapride.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "batches")
data class Batch(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val breedName: String,
    val startDate: Long, // Store as System.currentTimeMillis()
    val isActive: Boolean = true
)