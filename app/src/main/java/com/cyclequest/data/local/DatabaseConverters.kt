package com.cyclequest.data.local

import androidx.room.TypeConverter
import com.cyclequest.core.database.sync.SyncStatus

class DatabaseConverters {
    @TypeConverter
    fun toSyncStatus(value: String): SyncStatus = enumValueOf(value)

    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String = status.name
}
