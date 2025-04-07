package com.example.scout.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    @Volatile
    private var INSTANCE: ScoutingDatabase? = null

    fun getDatabase(context: Context): ScoutingDatabase {
        return INSTANCE ?: synchronized(this) {
            val instance = Room.databaseBuilder(
                context.applicationContext,
                ScoutingDatabase::class.java,
                "scouting_database"
            ).build()
            INSTANCE = instance
            instance
        }
    }
}
