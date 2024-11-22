package com.cyclequest.domain.model

import com.cyclequest.data.local.entity.PostStatus

data class PostImages (
    val id: String,
    val postId: String,
    val imageUrl: String,
    var orderIndex: String
)

