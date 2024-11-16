package com.cyclequest.data.local.dao

import com.cyclequest.data.local.TestDatabase
import com.cyclequest.data.local.entity.PostEntity
import com.cyclequest.data.local.entity.PostStatus
import com.cyclequest.core.database.sync.SyncStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class PostDaoTest : TestDatabase() {
    private val postDao by lazy { getDatabase().postDao() }

    @Test
    fun insertAndGetPost() = runBlocking {
        println("=== 测试插入和获取帖子 ===")
        val post = createTestPost()
        println("创建测试帖子: $post")
        postDao.insertPost(post)
        println("帖子已插入数据库")
        
        val loadedPost = postDao.getPostById(post.postId)
        println("从数据库加载帖子: $loadedPost")
        assertEquals(post, loadedPost)
        println("=== 测试完成 ===")
    }

    @Test
    fun getUserPosts() = runBlocking {
        println("=== 测试获取用户帖子 ===")
        val posts = listOf(
            createTestPost("1", "user1"),
            createTestPost("2", "user1"),
            createTestPost("3", "user2")
        )
        println("创建测试帖子列表: ${posts.joinToString()}")
        
        posts.forEach { 
            println("插入帖子: $it")
            postDao.insertPost(it)
        }
        println("所有帖子已插入数据库")
        
        val user1Posts = postDao.getUserPostsFlow("user1").first()
        println("获取user1的帖子: ${user1Posts.joinToString()}")
        assertEquals(2, user1Posts.size)
        println("=== 测试完成 ===")
    }

    @Test
    fun deletePost() = runBlocking {
        println("=== 测试删除帖子 ===")
        val post = createTestPost()
        println("创建测试帖子: $post")
        postDao.insertPost(post)
        println("帖子已插入数据库")
        
        postDao.deletePost(post.postId)
        println("帖子已从数据库删除")
        
        val loadedPost = postDao.getPostById(post.postId)
        println("尝试加载已删除帖子: $loadedPost")
        assertNull(loadedPost)
        println("=== 测试完成 ===")
    }

    private fun createTestPost(
        id: String = "test_post_id",
        userId: String = "test_user_id"
    ) = PostEntity(
        postId = id,
        userId = userId,
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
} 