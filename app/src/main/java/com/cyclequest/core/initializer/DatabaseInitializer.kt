package com.cyclequest.core.initializer

import android.content.Context
import androidx.room.Room
import androidx.room.RoomDatabase
import com.cyclequest.data.local.AppDatabase
import com.cyclequest.core.database.migration.DatabaseMigrationProvider
import com.cyclequest.core.database.DatabaseConfig
import javax.inject.Inject
import javax.inject.Singleton

// core/database/DatabaseInitializer.kt
@Singleton
class DatabaseInitializer @Inject constructor(
    private val context: Context,
    private val databaseConfig: DatabaseConfig,
    private val migrationProvider: DatabaseMigrationProvider,
    private val callback: RoomDatabase.Callback
) {
    private lateinit var database: AppDatabase

    fun initialize() {
        database = createDatabase()
    }

    private fun createDatabase(): AppDatabase {
        return Room.databaseBuilder(context, AppDatabase::class.java, databaseConfig.name)
            .setJournalMode(if (databaseConfig.enableWAL) RoomDatabase.JournalMode.WRITE_AHEAD_LOGGING else RoomDatabase.JournalMode.TRUNCATE)
            .addMigrations(*migrationProvider.getMigrations())
            .addCallback(callback)
            .build()
    }

    fun getDatabase(): AppDatabase = database
}
