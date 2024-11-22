package com.cyclequest.data.mapper


import com.cyclequest.data.local.entity.PostEntity
import com.cyclequest.data.local.entity.PostStatus
import com.cyclequest.data.local.entity.UserEntity
import com.cyclequest.domain.model.Post
import com.cyclequest.domain.model.User
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostMapper @Inject constructor() {
    fun toDomain(entity: PostEntity): Post = Post(
        postId = entity.postId,
        userId = entity.userId,
        title = entity.title,
        content = entity.content,
        achievementId = entity.achievementId,
        viewCount = entity.viewCount,
        likeCount = entity.likeCount,
        commentCount = entity.commentCount,
        isTop = entity.isTop,
        status = entity.status,
        createdAt = entity.createdAt,
        updatedAt = entity.updatedAt,
    )

    fun toLocal(domain: Post): PostEntity = PostEntity(
        postId = domain.postId,
        userId = domain.userId,
        title = domain.title,
        content = domain.content,
        achievementId = domain.achievementId,
        viewCount = domain.viewCount,
        likeCount = domain.likeCount,
        commentCount = domain.commentCount,
        isTop = domain.isTop,
        status = domain.status,
        createdAt = domain.createdAt,
        updatedAt = domain.updatedAt,
    )
}