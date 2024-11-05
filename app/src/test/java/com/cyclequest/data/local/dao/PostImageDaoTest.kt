package com.cyclequest.data.local.dao

import com.cyclequest.data.local.*
import com.cyclequest.data.local.entity.*
import com.cyclequest.core.database.sync.SyncStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PostImageDaoTest : TestDatabase() {
    private val postImageDao by lazy { getDatabase().postImageDao() }
    private val postDao by lazy { getDatabase().postDao() }

    @Test
    fun insertAndGetPostImages() = runBlocking {
        println("=== 测试插入和获取帖子图片 ===")
        val post = createTestPost("post1")
        println("创建测试帖子: $post")
        postDao.insertPost(post)
        println("帖子已插入数据库")
        
        val images = listOf(
            createTestPostImage("post1", 0),
            createTestPostImage("post1", 1),
            createTestPostImage("post1", 2)
        )
        println("创建测试图片列表: ${images.joinToString()}")
        
        postImageDao.insertAll(images)
        println("所有图片已插入数据库")
        
        val loadedImages = postImageDao.getPostImagesFlow("post1").first()
        println("从数据库加载的图片列表: ${loadedImages.joinToString()}")
        assertEquals(images.toSet(), loadedImages.toSet())
        println("=== 测试完成 ===")
    }

    @Test
    fun deletePostImages() = runBlocking {
        println("=== 测试删除帖子图片 ===")
        val post = createTestPost("post1")
        println("创建测试帖子: $post")
        postDao.insertPost(post)
        println("帖子已插入数据库")
        
        val image = createTestPostImage("post1", 0)
        println("创建测试图片: $image")
        postImageDao.insertPostImage(image)
        println("图片已插入数据库")
        
        postImageDao.deletePostImages("post1")
        println("图片已从数据库删除")
        
        val loadedImages = postImageDao.getPostImagesFlow("post1").first()
        println("尝试加载已删除图片: $loadedImages")
        assertTrue(loadedImages.isEmpty())
        println("=== 测试完成 ===")
    }

    private fun createTestPost(postId: String) = PostEntity(
        postId = postId,
        userId = "test_user_id",
        title = "Test Post",
        content = "Test Content",
        viewCount = 0,
        likeCount = 0,
        commentCount = 0,
        isTop = false,
        status = PostStatus.NORMAL,
        syncStatus = SyncStatus.PENDING,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )

    private fun createTestPostImage(
        postId: String = "test_post",
        orderIndex: Int = 0
    ) = PostImageEntity(
        postId = postId,
        imageUrl = "test_image_$orderIndex.jpg",
        orderIndex = orderIndex
    )
} 