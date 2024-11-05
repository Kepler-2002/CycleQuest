package com.cyclequest.data.local.entity

import androidx.room.*

@Entity(
    tableName = "comment_likes",
    foreignKeys = [
        ForeignKey(
            entity = CommentEntity::class,
            parentColumns = ["commentId"],
            childColumns = ["commentId"],
            onDelete = ForeignKey.CASCADE
        )
    ],
    indices = [Index("commentId"), Index("userId")]
)
data class CommentLikeEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val commentId: String,
    val userId: String,
    @ColumnInfo(name = "created_at")
    val createdAt: Long = System.currentTimeMillis()
) 