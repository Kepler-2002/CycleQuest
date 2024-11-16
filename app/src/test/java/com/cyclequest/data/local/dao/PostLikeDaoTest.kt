package com.cyclequest.data.local.dao

import com.cyclequest.data.local.TestDatabase
import com.cyclequest.data.local.entity.*
import com.cyclequest.core.database.sync.SyncStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertFalse
import kotlin.test.assertTrue

class PostLikeDaoTest : TestDatabase() {
    private val postLikeDao by lazy { getDatabase().postLikeDao() }
    private val postDao by lazy { getDatabase().postDao() }

    @Test
    fun insertAndGetPostLikes() = runBlocking {
        println("=== 测试插入和获取帖子点赞 ===")
        val post = createTestPost("post1")
        println("创建测试帖子: $post")
        postDao.insertPost(post)
        println("帖子已插入数据库")
        
        val likes = listOf(
            createTestPostLike("post1", "user1"),
            createTestPostLike("post1", "user2")
        )
        println("创建测试点赞列表: ${likes.joinToString()}")
        
        likes.forEach {
            println("插入点赞: $it") 
            postLikeDao.insertPostLike(it)
        }
        println("所有点赞已插入数据库")
        
        val loadedLikes = postLikeDao.getPostLikesFlow("post1").first()
        println("从数据库加载的点赞列表: ${loadedLikes.joinToString()}")
        assertEquals(likes.toSet(), loadedLikes.toSet())
        println("=== 测试完成 ===")
    }

    @Test
    fun getPostLikeCount() = runBlocking {
        println("=== 测试获取帖子点赞数 ===")
        val post1 = createTestPost("post1")
        val post2 = createTestPost("post2")
        println("创建测试帖子: $post1, $post2")
        postDao.insertPost(post1)
        postDao.insertPost(post2)
        println("帖子已插入数据库")
        
        val likes = listOf(
            createTestPostLike("post1", "user1"),
            createTestPostLike("post1", "user2"),
            createTestPostLike("post2", "user1")
        )
        println("创建测试点赞列表: ${likes.joinToString()}")
        
        likes.forEach { postLikeDao.insertPostLike(it) }
        println("所有点赞已插入数据库")
        
        val likeCount = postLikeDao.getPostLikeCountFlow("post1").first()
        println("post1的点赞数: $likeCount")
        assertEquals(2, likeCount)
        println("=== 测试完成 ===")
    }

    @Test
    fun isPostLikedByUser() = runBlocking {
        println("=== 测试检查用户是否点赞 ===")
        val post = createTestPost("post1")
        println("创建测试帖子: $post")
        postDao.insertPost(post)
        println("帖子已插入数据库")
        
        val like = createTestPostLike("post1", "user1")
        println("创建测试点赞: $like")
        postLikeDao.insertPostLike(like)
        println("点赞已插入数据库")
        
        val isLiked = postLikeDao.isPostLikedByUserFlow("post1", "user1").first()
        println("user1是否点赞post1: $isLiked")
        assertTrue(isLiked)
        
        val isLikedByOther = postLikeDao.isPostLikedByUserFlow("post1", "user2").first()
        println("user2是否点赞post1: $isLikedByOther")
        assertFalse(isLikedByOther)
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

    private fun createTestPostLike(
        postId: String = "test_post",
        userId: String = "test_user"
    ) = PostLikeEntity(
        postId = postId,
        userId = userId,
        createdAt = System.currentTimeMillis()
    )
} 