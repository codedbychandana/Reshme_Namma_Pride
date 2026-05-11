package com.example.reshme_nammapride.data.remote

import okhttp3.ResponseBody
import retrofit2.Response
import retrofit2.http.POST
import retrofit2.http.Path

/**
 * Interface for communicating with the Raspberry Pi (or Laptop Simulation).
 * This service handles the external control of climate-adjusting equipment.
 */
interface PiApiService {

    /**
     * Sends a command to toggle a specific hardware device.
     * * @param device The name of the unit (e.g., "heater", "fan", "humidifier")
     * @param action The desired state (e.g., "on", "off")
     */
    @POST("control/{device}/{action}")
    suspend fun toggleDevice(
        @Path("device") device: String,
        @Path("action") action: String
    ): Response<ResponseBody>
}