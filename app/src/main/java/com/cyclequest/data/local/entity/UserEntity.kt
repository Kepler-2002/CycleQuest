package com.cyclequest.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.ColumnInfo
import com.cyclequest.core.base.BaseEntity
import com.cyclequest.core.database.sync.SyncStatus

@Entity(tableName = "users")
data class UserEntity(

    //所有entity继承于BaseEntity的属性有: createdAt updatedAt
    // id 为什么不是 Int
    @PrimaryKey
    val id: String,
    val username: String,
    val email: String,
    val phoneNumber: String?,
    val password: String,
    val avatarUrl: String?,
    val status: UserStatus,
    val totalRides: Int,
    val totalDistance: Float,
    val totalRideTime: Long,
    @ColumnInfo(name = "last_login_at")
    val lastLoginAt: Long?,
    /*@ColumnInfo(name = "sync_status")
    override val syncStatus: SyncStatus = SyncStatus.PENDING,*/
    @ColumnInfo(name = "created_at")
    override val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    override val updatedAt: Long = System.currentTimeMillis()
) : BaseEntity
