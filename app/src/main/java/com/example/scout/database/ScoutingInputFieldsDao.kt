package com.example.scout.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScoutingInputFieldsDao {
    // Function to access all rows with a particular section name
    @Query("SELECT * FROM ScoutingInputFields WHERE section = :section")

    // Functions annotated with suspend to ensure they run asynchronously to the main thread
    // Prevents UI crash
    suspend fun getFieldsForSection(section: String): List<ScoutingInputFields>

    // Function to add a field to the database
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertField(field: ScoutingInputFields)

    // Function to delete a field from the database
    @Delete
    suspend fun deleteField(field: ScoutingInputFields)
}
