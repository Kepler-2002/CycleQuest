package com.cyclequest.data.local.entity

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface PostDao {
    @Query("SELECT * FROM posts")
    fun getAllPostsFlow(): Flow<List<PostEntity>>

    @Query("SELECT * FROM posts WHERE postId = :postId")
    suspend fun getPostById(postId: String): PostEntity?

    @Query("SELECT * FROM posts WHERE userId = :userId")
    fun getUserPostsFlow(userId: String): Flow<List<PostEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPost(post: PostEntity)

    @Update
    suspend fun updatePost(post: PostEntity)

    @Query("DELETE FROM posts WHERE postId = :postId")
    suspend fun deletePost(postId: String)

    @Query("UPDATE posts SET viewCount = viewCount + 1 WHERE postId = :postId")
    suspend fun incrementViewCount(postId: String)

    @Query("UPDATE posts SET likeCount = :likeCount WHERE postId = :postId")
    suspend fun updateLikeCount(postId: String, likeCount: Int)
} 