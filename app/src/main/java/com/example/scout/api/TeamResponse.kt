package com.example.scout.api
data class TeamResponse(
    val teamNumber: String,
    val nickname: String,
    val city: String,
    val stateProv: String,
    val country: String
    // Add other fields according to the API response
)
