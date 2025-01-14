package com.example.scout.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// Table named ScoutingInputFields for keeping track of input fields for scouting form
@Entity(tableName = "ScoutingInputFields")
data class ScoutingInputFields(
    // Primary key is simple integer that increases each time a row is added to the table
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    // Each value is a column in the data table
    // Section = autonomous, teleop, or endgame
    val section: String,
    // Name of field input (EX: coral scored)
    val fieldName: String,
    // Type of input field takes (number or dropdown)
    val fieldInputType: String,
    // List of dropdown options, null if fieldInputType is not dropdown
    val dropdownOptions: String? = null
)

