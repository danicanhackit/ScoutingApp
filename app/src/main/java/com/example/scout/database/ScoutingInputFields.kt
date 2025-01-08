package com.example.scout.database

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "ScoutingInputFields")
data class ScoutingInputFields(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val section: String,
    val fieldName: String,
    val fieldInputType: String,
    val dropdownOptions: String? = null
)

