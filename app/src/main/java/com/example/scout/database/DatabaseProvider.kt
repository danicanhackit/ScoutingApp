package com.example.scout.database

import android.content.Context
import androidx.room.Room

object DatabaseProvider {

    // Singleton instance of the database
    @Volatile
    private var INSTANCE: ScoutingDatabase? = null

    // Provides a thread-safe way to get the database instance
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
