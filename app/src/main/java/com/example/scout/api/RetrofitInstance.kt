package com.example.scout.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    // Base URL that all functions in BlueAllianceAPI interface add to
    // in order to create unique endpoints
    private const val BASE_URL = "https://www.thebluealliance.com/api/v3/"

    private val retrofit by lazy {
        // Builds Retrofit instance
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: BlueAllianceAPI by lazy {
        // Implements BlueAllianceAPI endpoints
        retrofit.create(BlueAllianceAPI::class.java)
    }
}
