package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.cyclequest.core.base.BaseEntity
import com.cyclequest.core.database.sync.SyncStatus

@Entity(tableName = "users")
data class UserEntity(
    @PrimaryKey
    override val id: String,
    val name: String,
    val email: String,
    @ColumnInfo(name = "sync_status")
    override val syncStatus: SyncStatus = SyncStatus.PENDING,
    @ColumnInfo(name = "created_at")
    override val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    override val updatedAt: Long = System.currentTimeMillis()
) : BaseEntity
