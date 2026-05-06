package com.example.reshme_nammapride.data.local.dao

import androidx.room.*
import com.example.reshme_nammapride.data.local.entity.Batch
import com.example.reshme_nammapride.data.local.entity.RearingRecord
import kotlinx.coroutines.flow.Flow

@Dao
interface RearingDao {
    // Batch Operations
    @Insert
    suspend fun insertBatch(batch: Batch): Long

    @Query("SELECT * FROM batches WHERE isActive = 1 LIMIT 1")
    fun getActiveBatch(): Flow<Batch?>

    // Record Operations
    @Insert
    suspend fun insertRecord(record: RearingRecord): Long

    @Query("SELECT * FROM rearing_records WHERE batchId = :batchId ORDER BY timestamp DESC")
    fun getRecordsForBatch(batchId: Int): Flow<List<RearingRecord>>
}