package com.cyclequest.data.local

import androidx.room.TypeConverter
import com.cyclequest.core.database.sync.SyncStatus
import com.cyclequest.data.local.entity.AchievementType
import com.cyclequest.data.local.entity.UserStatus

class DatabaseConverters {
    @TypeConverter
    fun toUserStatus(value: String): UserStatus = enumValueOf(value)

    @TypeConverter
    fun fromUserStatus(status: UserStatus): String = status.name

    @TypeConverter
    fun toAchievementType(value: String): AchievementType = enumValueOf(value)

    @TypeConverter
    fun fromAchievementType(type: AchievementType): String = type.name

    @TypeConverter
    fun toSyncStatus(value: String): SyncStatus = enumValueOf(value)

    @TypeConverter
    fun fromSyncStatus(status: SyncStatus): String = status.name
}
