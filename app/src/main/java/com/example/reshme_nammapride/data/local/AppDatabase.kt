package com.example.reshme_nammapride.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.reshmenamma.data.local.dao.RearingDao
import com.example.reshmenamma.data.local.entity.Batch
import com.example.reshmenamma.data.local.entity.RearingRecord

@Database(entities = [Batch::class, RearingRecord::class], version = 1)
@TypeConverters(Converters::class)
abstract class AppDatabase : RoomDatabase() {
    abstract fun rearingDao(): RearingDao
}