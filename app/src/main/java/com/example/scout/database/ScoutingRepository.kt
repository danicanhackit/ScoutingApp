package com.example.scout.database

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import com.example.scout.files.FileUtils
import java.text.SimpleDateFormat
import java.util.Date

class ScoutingRepository(private val scoutingFieldDao: ScoutingInputFieldsDao, private val scoutingReportDao: ScoutingReportDao) {
    val allInputFieldNames: LiveData<List<String>> = scoutingFieldDao.getAllInputFieldNames()

    // INPUT FIELDS
    // Suspend keyword ensures functions run asynchronously to main thread
    suspend fun getFieldsForSection(section: String): List<ScoutingInputFields> {
        // Calls function on scoutingFieldDao
        return scoutingFieldDao.getFieldsForSection(section)
    }
    suspend fun insertFieldToScoutingInputFields(field: ScoutingInputFields) {
        scoutingFieldDao.insertField(field)
    }
    suspend fun deleteFieldFromScoutingInputFields(field: ScoutingInputFields) {
        scoutingFieldDao.deleteField(field)
    }
    suspend fun getFieldByFieldName(fieldName: String): ScoutingInputFields{
        return scoutingFieldDao.getFieldByFieldName(fieldName)
    }

    // SCOUTING REPORTS
    suspend fun addScoutingReport(field: ScoutingReport){
        scoutingReportDao.addScoutingReport(field)
    }
    suspend fun deleteScoutingReport(field: ScoutingReport){
        scoutingReportDao.deleteScoutingReport(field)
    }
    suspend fun getReportsByIdAndSection(id: String, section: String): List<ScoutingReport>{
        return scoutingReportDao.getReportsByIdAndSection(id, section)
    }
    suspend fun getReportsByIdAndTeamNum(id: String, teamNum: Int?): List<ScoutingReport>{
        return scoutingReportDao.getReportsByIdAndTeamNum(id, teamNum)
    }
    suspend fun getReportsById(id: String): List<ScoutingReport>{
        return scoutingReportDao.getReportsById(id)
    }

    suspend fun exportReportById(context: Context, reportId: String, teamNum: String) {
        val reports = scoutingReportDao.getReportsById(reportId) // Fetch reports from the database
        val sdf = SimpleDateFormat("'Date:'dd-MM-yyyy'_Time:'HH:mm:ss z")
        val currentDateAndTime = sdf.format(Date())

        if (reports.isNotEmpty()) {
            FileUtils.exportDatabaseToCSV(context, "Team "+
                    teamNum+"_"+currentDateAndTime+".csv", reports)
        }
    }


    // DEFAULT DATABASE
    // Preload database with default fields if necessary
    suspend fun preloadDatabase() {
            val sections = listOf("Autonomous", "Teleop", "Endgame")
            sections.forEach { section ->
                val existingFields = scoutingFieldDao.getFieldsForSection(section)
                if (existingFields.isEmpty()) {
                    // Insert default fields for each section if not already in the database
                    val fields = getDefaultFieldsForSection(section)
                    Log.d("Database", "Database preloaded successfully")
                    fields.forEach { scoutingFieldDao.insertField(it) }
                }
            }
    }

    // Define default fields for each section
    private fun getDefaultFieldsForSection(section: String): List<ScoutingInputFields> {
        return when (section) {
            "Autonomous" -> listOf(
                ScoutingInputFields(section = "Autonomous", fieldName = "Left Barge", fieldInputType = "Dropdown", dropdownOptions = "Yes,No"),
                ScoutingInputFields(section = "Autonomous", fieldName = "Coral Scored", fieldInputType = "Number"),
                ScoutingInputFields(section = "Autonomous", fieldName = "Algae Removed", fieldInputType = "Number")
            )
            "Teleop" -> listOf(
                ScoutingInputFields(section = "Teleop", fieldName = "Coral L1 Scored", fieldInputType = "Number"),
                ScoutingInputFields(section = "Teleop", fieldName = "Coral L2 Scored", fieldInputType = "Number"),
                ScoutingInputFields(section = "Teleop", fieldName = "Coral L3 Scored", fieldInputType = "Number"),
                ScoutingInputFields(section = "Teleop", fieldName = "Coral L4 Scored", fieldInputType = "Number"),
                ScoutingInputFields(section = "Teleop", fieldName = "Algae Scored", fieldInputType = "Number"),
                ScoutingInputFields(section = "Teleop", fieldName = "Algae Processed", fieldInputType = "Number")
            )
            "Endgame" -> listOf(
                ScoutingInputFields(section = "Endgame", fieldName = "Climbed", fieldInputType = "Dropdown", dropdownOptions = "Shallow,Deep,Park")
            )
            else -> emptyList()
        }
    }
}
