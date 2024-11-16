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

class CommentLikeDaoTest : TestDatabase() {
    private val commentLikeDao by lazy { getDatabase().commentLikeDao() }
    private val commentDao by lazy { getDatabase().commentDao() }
    private val postDao by lazy { getDatabase().postDao() }

    @Test
    fun insertAndGetCommentLikes() = runBlocking {
        println("=== 测试插入和获取评论点赞 ===")
        val post = createTestPost("post1")
        println("创建测试帖子: $post")
        postDao.insertPost(post)
        
        val comment = createTestComment("comment1", "post1")
        println("创建测试评论: $comment")
        commentDao.insertComment(comment)
        
        val likes = listOf(
            createTestCommentLike("comment1", "user1"),
            createTestCommentLike("comment1", "user2")
        )
        println("创建测试点赞列表: ${likes.joinToString()}")
        
        likes.forEach {
            println("插入点赞: $it") 
            commentLikeDao.insertCommentLike(it)
        }
        println("所有点赞已插入数据库")
        
        val loadedLikes = commentLikeDao.getCommentLikesFlow("comment1").first()
        println("从数据库加载的点赞列表: ${loadedLikes.joinToString()}")
        
        val expectedLikes = likes.map { it.copy(id = 0) }.toSet()
        val actualLikes = loadedLikes.map { it.copy(id = 0) }.toSet()
        assertEquals(expectedLikes, actualLikes)
        println("=== 测试完成 ===")
    }

    @Test
    fun getCommentLikeCount() = runBlocking {
        println("=== 测试获取评论点赞数 ===")
        val post = createTestPost("post1")
        postDao.insertPost(post)
        
        val comment1 = createTestComment("comment1", "post1")
        val comment2 = createTestComment("comment2", "post1")
        commentDao.insertComment(comment1)
        commentDao.insertComment(comment2)
        
        val likes = listOf(
            createTestCommentLike("comment1", "user1"),
            createTestCommentLike("comment1", "user2"),
            createTestCommentLike("comment2", "user1")
        )
        println("创建测试点赞列表: ${likes.joinToString()}")
        
        likes.forEach { commentLikeDao.insertCommentLike(it) }
        println("所有点赞已插入数据库")
        
        val likeCount = commentLikeDao.getCommentLikeCountFlow("comment1").first()
        println("comment1的点赞数: $likeCount")
        assertEquals(2, likeCount)
        println("=== 测试完成 ===")
    }

    @Test
    fun isCommentLikedByUser() = runBlocking {
        println("=== 测试检查用户是否点赞评论 ===")
        val post = createTestPost("post1")
        println("创建测试帖子: $post")
        postDao.insertPost(post)

        val comment = createTestComment("comment1", "post1")
        println("创建测试评论: $comment")
        commentDao.insertComment(comment)
        val like = createTestCommentLike("comment1", "user1")
        println("创建测试点赞: $like")
        commentLikeDao.insertCommentLike(like)
        println("点赞已插入数据库")
        
        val isLiked = commentLikeDao.isCommentLikedByUserFlow("comment1", "user1").first()
        println("user1是否点赞comment1: $isLiked")
        assertTrue(isLiked)
        
        val isLikedByOther = commentLikeDao.isCommentLikedByUserFlow("comment1", "user2").first()
        println("user2是否点赞comment1: $isLikedByOther")
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

    private fun createTestComment(
        commentId: String,
        postId: String
    ) = CommentEntity(
        commentId = commentId,
        postId = postId,
        userId = "test_user_id",
        content = "Test Comment",
        likeCount = 0,
        parentId = null,
        status = CommentStatus.NORMAL,
        syncStatus = SyncStatus.PENDING,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )

    private fun createTestCommentLike(
        commentId: String = "test_comment",
        userId: String = "test_user"
    ) = CommentLikeEntity(
        commentId = commentId,
        userId = userId,
        createdAt = System.currentTimeMillis()
    )
} 