package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.PostTagEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostTagDao {
    @Query("SELECT * FROM post_tags WHERE postId = :postId")
    fun getPostTagsFlow(postId: String): Flow<List<PostTagEntity>>

    @Query("SELECT DISTINCT tag FROM post_tags")
    fun getAllTagsFlow(): Flow<List<String>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostTag(postTag: PostTagEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(postTags: List<PostTagEntity>)

    @Query("DELETE FROM post_tags WHERE postId = :postId")
    suspend fun deletePostTags(postId: String)

    @Query("DELETE FROM post_tags WHERE postId = :postId AND tag = :tag")
    suspend fun deletePostTag(postId: String, tag: String)
} 