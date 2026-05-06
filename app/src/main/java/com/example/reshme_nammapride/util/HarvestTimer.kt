package com.example.reshme_nammapride.util

import com.example.reshme_nammapride.data.local.entity.RearingRecord
import com.example.reshme_nammapride.domain.model.InstarStage

object HarvestTimer {
    fun calculateStatus(records: List<RearingRecord>): String? {
        // find the very first log entry for the 5th Instar
        val fifthInstarStart = records.firstOrNull { it.stage == InstarStage.FIFTH_INSTAR }?.timestamp
            ?: return null

        val msInDay = 24 * 60 * 60 * 1000L
        val daysPassed = (System.currentTimeMillis() - fifthInstarStart) / msInDay
        val daysRemaining = 7 - daysPassed
        return when {
            daysRemaining <= 0 -> "Time to Transfer to Spinning Trays!"
            else -> "$daysRemaining days until spinning starts"
        }
    }
}