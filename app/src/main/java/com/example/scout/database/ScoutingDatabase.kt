package com.example.scout.database

import androidx.room.Database
import androidx.room.RoomDatabase

// Array of all entities associated with the database
@Database(entities = [ScoutingInputFields::class, ScoutingReport::class], version = 1)
abstract class ScoutingDatabase : RoomDatabase() {
    // Database class must contain a method for each DAO
    abstract fun scoutingInputFieldsDao(): ScoutingInputFieldsDao
    abstract fun scoutingReportDao(): ScoutingReportDao
}

