package com.example.scout.api
import com.google.gson.annotations.SerializedName

data class TeamEventResponse(
    @SerializedName("name") val name: String
)
