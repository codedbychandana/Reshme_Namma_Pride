package com.example.reshme_nammapride.domain.logic

import com.example.reshme_nammapride.R
import com.example.reshme_nammapride.domain.model.InstarStage

data class ClimateAdvice(
    val status: ClimateStatus,
    val messageResId: Int
)

enum class ClimateStatus { SAFE, CAUTION, DANGER }

// smart advice engine which compares readings with ideal growth curve and gives smart suggestions
object ClimateEngine {

    fun analyze(stage: InstarStage, temp: Float, humidity: Float): ClimateAdvice {
        return when (stage) {
            InstarStage.FIRST_INSTAR, InstarStage.SECOND_INSTAR -> {
                checkRanges(temp, 26f, 28f + 1f, humidity, 80f, 85f + 1f)
            }
            InstarStage.THIRD_INSTAR -> {
                checkRanges(temp, 24f, 26f + 1f, humidity, 75f, 80f + 1f)
            }
            InstarStage.FOURTH_INSTAR -> {
                checkRanges(temp, 24f, 25f + 1f, humidity, 65f, 75f + 1f)
            }
            InstarStage.FIFTH_INSTAR -> {
                checkRanges(temp, 23f, 24f + 1f, humidity, 60f, 70f + 1f)
            }
        }
    }

    private fun checkRanges(
        temp: Float, minTemp: Float, maxTemp: Float,
        hum: Float, minHum: Float, maxHum: Float
    ): ClimateAdvice {

        return when {
            temp > maxTemp + 2 ->
                ClimateAdvice(ClimateStatus.DANGER, R.string.advice_critical_high_temp)
            temp < minTemp - 2 ->
                ClimateAdvice(ClimateStatus.DANGER, R.string.advice_critical_low_temp)
            temp < minTemp ->
                    ClimateAdvice(ClimateStatus.CAUTION, R.string.advice_low_temp)
            temp > maxTemp ->
                ClimateAdvice(ClimateStatus.CAUTION, R.string.advice_high_temp)
            hum >= maxHum + 10 ->
                ClimateAdvice(ClimateStatus.DANGER, R.string.advice_critical_high_hum)
            hum <= minHum - 10 ->
                ClimateAdvice(ClimateStatus.DANGER, R.string.advice_critical_low_hum)
            hum < minHum ->
                ClimateAdvice(ClimateStatus.CAUTION, R.string.advice_dry_air)
            hum > maxHum ->
                ClimateAdvice(ClimateStatus.CAUTION, R.string.advice_humid_air)
            else ->
                ClimateAdvice(ClimateStatus.SAFE, R.string.advice_ideal)
        }
    }

    fun getTargetTemperature(stage: InstarStage): Float {
        return when (stage) {
            InstarStage.FIRST_INSTAR, InstarStage.SECOND_INSTAR -> 27f
            InstarStage.THIRD_INSTAR -> 25.5f
            InstarStage.FOURTH_INSTAR, InstarStage.FIFTH_INSTAR -> 24f
        }
    }

    fun getTargetHumidity(stage: InstarStage): Float {
        return when (stage) {
            InstarStage.FIRST_INSTAR, InstarStage.SECOND_INSTAR -> 85f
            InstarStage.THIRD_INSTAR -> 77.5f
            InstarStage.FOURTH_INSTAR, InstarStage.FIFTH_INSTAR -> 70f
        }
    }
}