package com.example.scout.database

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query

@Dao
interface ScoutingInputFieldsDao {
    @Query("SELECT * FROM ScoutingInputFields WHERE section = :section")
    fun getFieldsForSection(section: String): List<ScoutingInputFields>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertField(field: ScoutingInputFields)

    @Delete
    fun deleteField(field: ScoutingInputFields)
}
