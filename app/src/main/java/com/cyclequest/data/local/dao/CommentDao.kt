package com.cyclequest.data.local.entity

import androidx.room.*
import kotlinx.coroutines.flow.Flow

@Dao
interface CommentDao {
    @Query("SELECT * FROM comments WHERE postId = :postId")
    fun getPostCommentsFlow(postId: String): Flow<List<CommentEntity>>

    @Query("SELECT * FROM comments WHERE commentId = :commentId")
    suspend fun getCommentById(commentId: String): CommentEntity?

    @Query("SELECT * FROM comments WHERE parentId = :parentId")
    fun getChildCommentsFlow(parentId: String): Flow<List<CommentEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertComment(comment: CommentEntity)

    @Update
    suspend fun updateComment(comment: CommentEntity)

    @Query("DELETE FROM comments WHERE commentId = :commentId")
    suspend fun deleteComment(commentId: String)

    @Query("UPDATE comments SET likeCount = :likeCount WHERE commentId = :commentId")
    suspend fun updateLikeCount(commentId: String, likeCount: Int)
} 