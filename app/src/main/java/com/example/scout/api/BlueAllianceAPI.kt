package com.example.scout.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BlueAllianceService {
    @GET("team/frc{teamNumber}/simple")
    fun getTeam(
        @Path("teamNumber") teamNumber: String,
        @Header("X-TBA-Auth-Key") apiKey: String
    ): Call<TeamResponse>  // Use TeamResponse directly, not a list
}
