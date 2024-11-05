package com.cyclequest.data.local.dao

import com.cyclequest.data.local.TestDatabase
import com.cyclequest.data.local.entity.*
import com.cyclequest.core.database.sync.SyncStatus
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking
import org.junit.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class PostTagDaoTest : TestDatabase() {
    private val postTagDao by lazy { getDatabase().postTagDao() }
    private val postDao by lazy { getDatabase().postDao() }

    @Test
    fun insertAndGetPostTags() = runBlocking {
        println("=== 测试插入和获取帖子标签 ===")
        val post = createTestPost("post1")
        println("创建测试帖子: $post")
        postDao.insertPost(post)
        println("帖子已插入数据库")
        
        val tags = listOf(
            createTestPostTag("post1", "tag1"),
            createTestPostTag("post1", "tag2"),
            createTestPostTag("post1", "tag3")
        )
        println("创建测试标签列表: ${tags.joinToString()}")
        
        postTagDao.insertAll(tags)
        println("所有标签已插入数据库")
        
        val loadedTags = postTagDao.getPostTagsFlow("post1").first()
        println("从数据库加载的标签列表: ${loadedTags.joinToString()}")
        assertEquals(tags.toSet(), loadedTags.toSet())
        println("=== 测试完成 ===")
    }

    @Test
    fun getAllDistinctTags() = runBlocking {
        println("=== 测试获取所有不重复标签 ===")
        val posts = listOf(
            createTestPost("post1"),
            createTestPost("post2"),
            createTestPost("post3")
        )
        posts.forEach { postDao.insertPost(it) }
        
        val tags = listOf(
            createTestPostTag("post1", "tag1"),
            createTestPostTag("post2", "tag1"),
            createTestPostTag("post3", "tag2")
        )
        println("创建测试标签列表: ${tags.joinToString()}")
        
        postTagDao.insertAll(tags)
        println("所有标签已插入数据库")
        
        val distinctTags = postTagDao.getAllTagsFlow().first()
        println("从数据库加载的不重复标签列表: $distinctTags")
        assertEquals(setOf("tag1", "tag2"), distinctTags.toSet())
        println("=== 测试完成 ===")
    }

    @Test
    fun deletePostTag() = runBlocking {
        println("=== 测试删除帖子标签 ===")
        val post = createTestPost("post1")
        println("创建测试帖子: $post")
        postDao.insertPost(post)
        println("帖子已插入数据库")
        
        val tag = createTestPostTag("post1", "tag1")
        println("创建测试标签: $tag")
        postTagDao.insertPostTag(tag)
        println("标签已插入数据库")
        
        postTagDao.deletePostTag("post1", "tag1")
        println("标签已从数据库删除")
        
        val loadedTags = postTagDao.getPostTagsFlow("post1").first()
        println("尝试加载已删除标签: $loadedTags")
        assertTrue(loadedTags.isEmpty())
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

    private fun createTestPostTag(
        postId: String = "test_post",
        tag: String = "test_tag"
    ) = PostTagEntity(
        postId = postId,
        tag = tag
    )
} 