package com.example.reshme_nammapride.domain.logic

import com.example.reshme_nammapride.domain.model.InstarStage

data class ClimateAdvice(
    val status: ClimateStatus,
    val message: String
)

enum class ClimateStatus { SAFE, CAUTION, DANGER }

object ClimateEngine {

    fun analyze(stage: InstarStage, temp: Float, humidity: Float): ClimateAdvice {
        return when (stage) {
            InstarStage.FIRST_INSTAR, InstarStage.SECOND_INSTAR -> {
                // Chawki worms need high heat and high humidity
                checkRanges(temp, 26f, 28f, humidity, 80f, 90f)
            }
            InstarStage.THIRD_INSTAR -> {
                checkRanges(temp, 25f, 26f, humidity, 75f, 80f)
            }
            InstarStage.FOURTH_INSTAR, InstarStage.FIFTH_INSTAR -> {
                // Late age worms need cooler, airier conditions
                checkRanges(temp, 23f, 25f, humidity, 65f, 75f)
            }
        }
    }

    private fun checkRanges(
        temp: Float, minTemp: Float, maxTemp: Float,
        hum: Float, minHum: Float, maxHum: Float
    ): ClimateAdvice {
        return when {
            temp > maxTemp + 2 || temp < minTemp - 2 ->
                ClimateAdvice(ClimateStatus.DANGER, "Critical! Move worms to a cooler area and use fans.")

            temp > maxTemp ->
                ClimateAdvice(ClimateStatus.CAUTION, "Temperature high. Open windows and spread wet gunny bags.")

            hum < minHum ->
                ClimateAdvice(ClimateStatus.CAUTION, "Air is too dry. Spray water on the floor/walls.")

            else ->
                ClimateAdvice(ClimateStatus.SAFE, "Conditions are ideal for healthy growth.")
        }
    }
}