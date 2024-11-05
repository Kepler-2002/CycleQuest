package com.cyclequest.data.local.dao

import com.cyclequest.data.local.TestDatabase
import com.cyclequest.data.local.entity.CommentEntity
import com.cyclequest.data.local.entity.CommentStatus
import com.cyclequest.data.local.entity.PostEntity
import com.cyclequest.data.local.entity.PostStatus
import com.cyclequest.core.database.sync.SyncStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

class CommentDaoTest : TestDatabase() {
    private val commentDao by lazy { getDatabase().commentDao() }
    private val postDao by lazy { getDatabase().postDao() }

    @Test
    fun insertAndGetComment() = runBlocking {
        println("=== 测试插入和获取评论 ===")
        val post = createTestPost("test_post_id")
        println("创建测试帖子: $post")
        postDao.insertPost(post)
        println("帖子已插入数据库")
        
        val comment = createTestComment()
        println("创建测试评论: $comment")
        commentDao.insertComment(comment)
        println("评论已插入数据库")
        
        val loadedComment = commentDao.getCommentById(comment.commentId)
        println("从数据库加载评论: $loadedComment")
        assertEquals(comment, loadedComment)
        println("=== 测试完成 ===")
    }

    @Test
    fun getPostComments() = runBlocking {
        println("=== 测试获取帖子评论 ===")
        val post1 = createTestPost("post1")
        val post2 = createTestPost("post2")
        println("创建测试帖子: $post1, $post2")
        postDao.insertPost(post1)
        postDao.insertPost(post2)
        println("帖子已插入数据库")
        
        val comments = listOf(
            createTestComment("1", "post1"),
            createTestComment("2", "post1"),
            createTestComment("3", "post2")
        )
        println("创建测试评论列表: ${comments.joinToString()}")
        
        comments.forEach { 
            println("插入评论: $it")
            commentDao.insertComment(it)
        }
        println("所有评论已插入数据库")
        
        val post1Comments = commentDao.getPostCommentsFlow("post1").first()
        println("获取post1的评论: ${post1Comments.joinToString()}")
        assertEquals(2, post1Comments.size)
        println("=== 测试完成 ===")
    }

    @Test
    fun getChildComments() = runBlocking {
        println("=== 测试获取子评论 ===")
        val post1 = createTestPost("post1")
        println("创建测试帖子: $post1")
        postDao.insertPost(post1)
        println("帖子已插入数据库")

        val parentComment = createTestComment("parent", "post1")
        val childComments = listOf(
            createTestComment("child1", "post1", "parent"),
            createTestComment("child2", "post1", "parent")
        )
        println("创建父评论: $parentComment")
        println("创建子评论列表: ${childComments.joinToString()}")
        
        commentDao.insertComment(parentComment)
        childComments.forEach { commentDao.insertComment(it) }
        println("所有评论已插入数据库")
        
        val loadedChildComments = commentDao.getChildCommentsFlow("parent").first()
        println("获取父评论的子评论: ${loadedChildComments.joinToString()}")
        assertEquals(2, loadedChildComments.size)
        println("=== 测试完成 ===")
    }

    private fun createTestPost(
        postId: String
    ) = PostEntity(
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
        commentId: String = "test_comment_id",
        postId: String = "test_post_id",
        parentId: String? = null
    ) = CommentEntity(
        commentId = commentId,
        postId = postId,
        userId = "test_user_id",
        content = "Test Comment",
        likeCount = 0,
        parentId = parentId,
        status = CommentStatus.NORMAL,
        syncStatus = SyncStatus.PENDING,
        createdAt = System.currentTimeMillis(),
        updatedAt = System.currentTimeMillis()
    )
}