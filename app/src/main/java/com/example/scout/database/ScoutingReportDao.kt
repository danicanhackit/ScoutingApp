package com.example.scout.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScoutingReportDao {

    @Query("SELECT * FROM ScoutingReport")
    suspend fun getAllScoutingReports(): List<ScoutingReport>

    @Query("SELECT * FROM ScoutingReport WHERE reportId = :reportId")
    suspend fun getReportsById(reportId: Int): List<ScoutingReport>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addScoutingReport(field: ScoutingReport)

    @Delete
    suspend fun deleteScoutingReport(field: ScoutingReport)
}
