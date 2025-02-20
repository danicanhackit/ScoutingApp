package com.example.scout.database

import androidx.room.Entity
import androidx.room.PrimaryKey

// Table named ScoutingInputFields for keeping track of input fields for scouting form
@Entity(tableName = "ScoutingReport")
data class ScoutingReport(
    // Primary key is simple integer that increases each time a row is added to the table
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val reportId: String,
    val teamNumberBeingScouted: Int?,
    //val teamNameBeingScouted: String,
    // auton, teleop, endgame
    val gameplaySection: String,
    // need to write some sort of code that pulls in all the existing fields from the scoutinginputfields table
    val fieldName: String,
    val enteredValue: String
    )
