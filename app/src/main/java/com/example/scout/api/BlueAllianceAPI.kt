package com.example.scout.api

import android.app.usage.UsageEvents
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface BlueAllianceService {
    @GET("team/frc{teamNumber}/events/{year}/simple")
    fun getTeamEvents(
        @Path("teamNumber") teamNumber: String,
        @Path("year") year: Int
    ): Call<List<UsageEvents.Event>>
}
