package com.cyclequest.core.database.migration

import androidx.room.migration.Migration
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class DatabaseMigrationProvider @Inject constructor() {
    fun getMigrations(): Array<Migration> {
        return arrayOf(
            // 在这里添加数据库迁移
        )
    }
}

