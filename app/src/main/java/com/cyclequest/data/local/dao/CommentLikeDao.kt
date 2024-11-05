package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.CommentLikeEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentLikeDao {
    @Query("SELECT * FROM comment_likes WHERE commentId = :commentId")
    fun getCommentLikesFlow(commentId: String): Flow<List<CommentLikeEntity>>

    @Query("SELECT COUNT(*) FROM comment_likes WHERE commentId = :commentId")
    fun getCommentLikeCountFlow(commentId: String): Flow<Int>

    @Query("SELECT EXISTS(SELECT 1 FROM comment_likes WHERE commentId = :commentId AND userId = :userId)")
    fun isCommentLikedByUserFlow(commentId: String, userId: String): Flow<Boolean>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertCommentLike(commentLike: CommentLikeEntity)

    @Query("DELETE FROM comment_likes WHERE commentId = :commentId AND userId = :userId")
    suspend fun deleteCommentLike(commentId: String, userId: String)
} 