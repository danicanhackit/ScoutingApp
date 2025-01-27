package com.example.scout.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScoutingReportDao {

    @Query("SELECT * FROM ScoutingReport")
    suspend fun getAllScoutingReports()

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertNewFieldToScoutingReport(field: ScoutingReport)

    @Delete
    suspend fun deleteFieldsFromScoutingReport(field: ScoutingReport)
}
