package com.cyclequest.data.local

import android.content.Context
import androidx.room.Room
import androidx.test.core.app.ApplicationProvider
import org.junit.After
import org.junit.Before
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@RunWith(RobolectricTestRunner::class)
@Config(sdk = [29])
abstract class TestDatabase {
    private lateinit var db: AppDatabase

    @Before
    fun createDb() {
        println("=== 开始创建测试数据库 ===")
        val context = ApplicationProvider.getApplicationContext<Context>()
        db = Room.inMemoryDatabaseBuilder(
            context, AppDatabase::class.java
        )
        .allowMainThreadQueries()
        .build()
        println("=== 测试数据库创建完成 ===")
    }

    @After
    fun closeDb() {
        println("=== 清理测试数据库 ===")
        db.close()
    }

    protected fun getDatabase() = db
} 