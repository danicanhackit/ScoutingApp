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
    suspend fun getReportsById(reportId: String): List<ScoutingReport>

    @Query("SELECT * FROM ScoutingReport WHERE gameplaySection = :gameplaySection")
    suspend fun getReportsBySection(gameplaySection: String): List<ScoutingReport>

    @Query("SELECT * FROM ScoutingReport WHERE reportId = :reportId AND gameplaySection = :gameplaySection")
    suspend fun getReportsByIdAndSection(reportId: String, gameplaySection: String): List<ScoutingReport>

    @Query("SELECT * FROM ScoutingReport WHERE reportId = :reportId AND teamNumberBeingScouted = :teamNum")
    suspend fun getReportsByIdAndTeamNum(reportId: String, teamNum: Int?): List<ScoutingReport>


    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addScoutingReport(field: ScoutingReport)

    @Delete
    suspend fun deleteScoutingReport(field: ScoutingReport)
}
