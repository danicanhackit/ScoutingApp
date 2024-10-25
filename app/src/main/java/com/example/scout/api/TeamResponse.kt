package com.example.scout.api
data class TeamResponse(
    val team_number: String,
    val nickname: String,
    val city: String,
    val state_prov: String,
    val country: String
    // Add other fields according to the API response
)
