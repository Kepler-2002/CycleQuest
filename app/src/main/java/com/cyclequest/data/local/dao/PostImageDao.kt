package com.cyclequest.data.local.dao

import androidx.room.*
import com.cyclequest.data.local.entity.PostImageEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface PostImageDao {
    @Query("SELECT * FROM post_images WHERE postId = :postId ORDER BY orderIndex")
    fun getPostImagesFlow(postId: String): Flow<List<PostImageEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertPostImage(postImage: PostImageEntity)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAll(postImages: List<PostImageEntity>)

    @Update
    suspend fun updatePostImage(postImage: PostImageEntity)

    @Query("DELETE FROM post_images WHERE postId = :postId")
    suspend fun deletePostImages(postId: String)

    @Transaction
    suspend fun reorderPostImages(images: List<PostImageEntity>) {
        images.forEachIndexed { index, image ->
            updatePostImage(image.copy(orderIndex = index))
        }
    }
} 