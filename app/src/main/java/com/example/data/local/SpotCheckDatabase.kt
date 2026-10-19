package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [
        SolvedCaseEntity::class,
        UserStatsEntity::class,
        BadgeEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class SpotCheckDatabase : RoomDatabase() {
    abstract fun spotCheckDao(): SpotCheckDao

    companion object {
        @Volatile
        private var INSTANCE: SpotCheckDatabase? = null

        fun getDatabase(context: Context): SpotCheckDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    SpotCheckDatabase::class.java,
                    "spotcheck_database"
                ).build()
                INSTANCE = instance
                instance
            }
        }
    }
}
