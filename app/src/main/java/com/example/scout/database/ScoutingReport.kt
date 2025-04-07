package com.example.scout.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// Table named ScoutingInputFields for keeping track of input fields for scouting form
@Entity(tableName = "ScoutingReport")
data class ScoutingReport(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val reportId: String,
    val teamNumberBeingScouted: Int?,
    // auton, teleop, endgame
    val gameplaySection: String,
    val fieldName: String,
    val enteredValue: String
    )
