package com.example.scout.database

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [ScoutingInputFields::class, ScoutingReport::class], version = 1)
abstract class ScoutingDatabase : RoomDatabase() {

    abstract fun scoutingInputFieldsDao(): ScoutingInputFieldsDao
    abstract fun scoutingReportDao(): ScoutingReportDao
}

