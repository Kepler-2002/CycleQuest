package com.cyclequest.domain.repository

import com.cyclequest.domain.model.Post
import com.cyclequest.data.local.dao.PostDao
import com.cyclequest.data.local.entity.PostEntity
import com.cyclequest.data.mapper.PostMapper
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class PostRepository @Inject constructor(private val postDao: PostDao, private val postMapper: PostMapper) {

    suspend fun savePost(post: PostEntity) {
        withContext(Dispatchers.IO) {
            postDao.insertPost(post)
        }
    }

    fun getUserPostsFlow(userId: String): Flow<List<Post>> {
        return postDao.getUserPostsFlow(userId).map { postEntities ->
            postEntities.map { postMapper.toDomain(it) }
        }
    }

    suspend fun getNextPostId(userId: String): Int {
        val posts = getUserPostsFlow(userId).first()
        val maxPostId = posts.map { it.postId.toInt() }.maxOrNull() ?: 0
        return maxPostId + 1
    }

}