package com.cyclequest.core.database.migration

import androidx.room.migration.Migration
import androidx.sqlite.db.SupportSQLiteDatabase
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseMigrationProvider @Inject constructor() {
    fun getMigrations(): Array<Migration> {
        return arrayOf(
            object : Migration(2, 3) {
                override fun migrate(database: SupportSQLiteDatabase) {
                    // 重新创建成就表
                    database.execSQL("DROP TABLE IF EXISTS achievements")
                    database.execSQL("""
                        CREATE TABLE achievements (
                            id TEXT PRIMARY KEY NOT NULL,
                            name TEXT NOT NULL,
                            description TEXT NOT NULL,
                            type TEXT NOT NULL,
                            requirement REAL NOT NULL,
                            resourceId INTEGER NOT NULL,
                            created_at INTEGER NOT NULL,
                            updated_at INTEGER NOT NULL
                        )
                    """)
                }
            }
        )
    }
}

