package com.cyclequest.data.local.entity


import androidx.room.*
import com.cyclequest.core.base.BaseEntity
import com.cyclequest.core.database.sync.SyncStatus


@Entity(tableName = "posts")
data class PostEntity(
    @PrimaryKey
    val postId: String,
    val userId: String,
    val title: String,
    val content: String,
    val achievementId: String,
    val viewCount: Int = 0,
    val likeCount: Int = 0,
    val commentCount: Int = 0,
    val isTop: Boolean = false,
    val status: PostStatus = PostStatus.NORMAL,
    @ColumnInfo(name = "created_at")
    override val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "updated_at")
    override val updatedAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus = SyncStatus.PENDING,
) : BaseEntity 