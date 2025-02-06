package com.example.scout.database

import android.util.Log

class ScoutingRepository(private val scoutingFieldDao: ScoutingInputFieldsDao, private val scoutingReportDao: ScoutingReportDao) {

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

    suspend fun addScoutingReport(field: ScoutingReport){
        scoutingReportDao.addScoutingReport(field)
    }
    suspend fun deleteScoutingReport(field: ScoutingReport){
        scoutingReportDao.deleteScoutingReport(field)
    }

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
