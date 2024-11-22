package com.cyclequest.data.local.entity

import androidx.room.*
import com.cyclequest.core.base.BaseEntity
import com.cyclequest.core.database.sync.SyncStatus

@Entity(
    tableName = "comments",
    foreignKeys = [
        ForeignKey(
            entity = PostEntity::class,
            parentColumns = ["postId"],
            childColumns = ["postId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("postId"), Index("userId")]
)
data class CommentEntity(
    @PrimaryKey
    val commentId: String,
    val postId: String,
    val userId: String,
    val content: String,
    val likeCount: Int = 0,
    val parentId: String?,
    val status: CommentStatus = CommentStatus.NORMAL,
    @ColumnInfo(name = "created_at")
    override val createdAt: Long = System.currentTimeMillis(),
    @ColumnInfo(name = "sync_status")
    val syncStatus: SyncStatus = SyncStatus.PENDING, override val updatedAt: Long
) : BaseEntity