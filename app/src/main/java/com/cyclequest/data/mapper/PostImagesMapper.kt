package com.cyclequest.data.mapper

import com.cyclequest.data.local.entity.PostImageEntity
import com.cyclequest.domain.model.PostImages
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostImagesMapper @Inject constructor() {

    fun toDomain(entity: PostImageEntity): PostImages = PostImages(
        id = entity.id.toString(),
        postId = entity.postId,
        imageUrl = entity.imageUrl,
        orderIndex = entity.orderIndex.toString()
    )

    fun toLocal(domain: PostImages): PostImageEntity = PostImageEntity(
        id = domain.id.toLong(),
        postId = domain.postId,
        imageUrl = domain.imageUrl,
        orderIndex = domain.orderIndex.toInt()
    )
}