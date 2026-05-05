package com.example.reshme_nammapride.data.local

import androidx.room.TypeConverter
import com.example.reshme_nammapride.domain.model.InstarStage

class Converters {
    @TypeConverter
    fun fromInstarStage(value: InstarStage): String {
        return value.name
    }

    @TypeConverter
    fun toInstarStage(value: String): InstarStage {
        return InstarStage.valueOf(value)
    }
}