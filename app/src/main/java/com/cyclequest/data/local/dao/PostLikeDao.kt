package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.PostLikeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostLikeDao {
    @Query("SELECT * FROM post_likes WHERE postId = :postId")
    fun getPostLikesFlow(postId: String): Flow<List<PostLikeEntity>>

    @Query("SELECT COUNT(*) FROM post_likes WHERE postId = :postId")
    fun getPostLikeCountFlow(postId: String): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM post_likes WHERE postId = :postId AND userId = :userId)")
    fun isPostLikedByUserFlow(postId: String, userId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostLike(postLike: PostLikeEntity)

    @Query("DELETE FROM post_likes WHERE postId = :postId AND userId = :userId")
    suspend fun deletePostLike(postId: String, userId: String)
} 