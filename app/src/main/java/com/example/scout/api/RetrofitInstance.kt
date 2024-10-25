package com.example.scout.api

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitInstance {
    private const val BASE_URL = "https://www.thebluealliance.com/api/v3/"

    private val retrofit by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    val api: BlueAllianceAPI by lazy {
        retrofit.create(BlueAllianceAPI::class.java)
    }
}
