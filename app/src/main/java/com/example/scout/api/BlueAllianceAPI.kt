package com.example.scout.api

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Path

interface BlueAllianceAPI {

    // Get request for simple team response matching team/simple API endpoint
    @GET("team/frc{teamNumber}/simple")
    fun getTeam(
        // Function takes in parameters of teamNumber (user input) and API key
        @Path("teamNumber") teamNumber: String,
        @Header("X-TBA-Auth-Key") apiKey: String
    ): Call<TeamResponse>  // Uses TeamResponse directly, not a list

    // Get request for simple event list response matching events/simple API endpoint
    @GET("team/frc{teamNumber}/events/{year}/simple")
    fun getTeamEvents(
        // Function takes in parameters of teamNumber, year (user input) and API key
        @Path("teamNumber") teamNumber: String,
        @Path("year") year: Int,
        @Header("X-TBA-Auth-Key") apiKey: String
    ): Call<List<TeamEventResponse>> // Returns a list of events
}
