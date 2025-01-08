package com.example.scout.database

class ScoutingRepository(private val scoutingFieldDao: ScoutingInputFieldsDao) {

    suspend fun getFieldsForSection(section: String): List<ScoutingInputFields> {
        return scoutingFieldDao.getFieldsForSection(section)
    }
    suspend fun insertField(field: ScoutingInputFields) {
        scoutingFieldDao.insertField(field)
    }
    suspend fun deleteField(field: ScoutingInputFields) {
        scoutingFieldDao.deleteField(field)
    }

    // Preload database with default fields if necessary
    suspend fun preloadDatabase() {
        val sections = listOf("Autonomous", "Teleop", "Endgame")
        sections.forEach { section ->
            val existingFields = scoutingFieldDao.getFieldsForSection(section)
            if (existingFields.isEmpty()) {
                // Insert default fields for each section if not already in the database
                val fields = getDefaultFieldsForSection(section)
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
