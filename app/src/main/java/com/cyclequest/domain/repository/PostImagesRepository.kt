package com.cyclequest.domain.repository

import com.cyclequest.domain.model.PostImages
import com.cyclequest.data.local.dao.PostImageDao
import com.cyclequest.data.local.entity.PostImageEntity
import com.cyclequest.data.mapper.PostImagesMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostImagesRepository @Inject constructor(
    private val postImageDao: PostImageDao,
    private val postImageMapper: PostImagesMapper
) {

    suspend fun savePostImage(postImage: PostImageEntity) {
        withContext(Dispatchers.IO) {
            postImageDao.insertPostImage(postImage)
        }
    }

    fun getPostImagesFlow(postId: String): Flow<List<PostImages>> {
        return postImageDao.getPostImagesFlow(postId).map { postImageEntities ->
            postImageEntities.map { postImageMapper.toDomain(it) }
        }
    }

    suspend fun reorderPostImages(images: List<PostImageEntity>) {
        withContext(Dispatchers.IO) {
            postImageDao.reorderPostImages(images)
        }
    }
}