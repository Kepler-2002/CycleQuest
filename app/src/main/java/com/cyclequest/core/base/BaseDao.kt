package com.cyclequest.core.base

import androidx.room.Transaction

// core/base/BaseDao.kt
interface BaseDao<T> {
    suspend fun insert(item: T)
    suspend fun insert(items: List<T>)
    suspend fun update(item: T)
    suspend fun delete(item: T)

    @Transaction
    suspend fun upsert(entity: T) {
        // 统一的 upsert 实现
    }
}