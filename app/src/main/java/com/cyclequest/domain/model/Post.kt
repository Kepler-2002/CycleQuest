package com.cyclequest.domain.model

import androidx.room.ColumnInfo
import androidx.room.PrimaryKey
import com.cyclequest.core.database.sync.SyncStatus
import com.cyclequest.data.local.entity.PostStatus
import com.cyclequest.data.local.entity.UserStatus

data class Post(
    val postId: String,
    val userId: String,
    val title: String,
    val content: String,
    val viewCount: Int,
    val likeCount: Int,
    val commentCount: Int,
    val isTop: Boolean,
    val status: PostStatus,
    val createdAt: Long,
    val updatedAt: Long
)