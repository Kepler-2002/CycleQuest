package com.cyclequest.data.sync.adapters

import com.cyclequest.core.base.BaseEntity

interface SyncAdapter<T : BaseEntity> {
    suspend fun getAll(): List<T>
    suspend fun getById(id: String): T?
    suspend fun getByIds(ids: List<String>): List<T>
    suspend fun update(entities: List<T>): List<T>
    suspend fun updateOne(entity: T): T
    suspend fun delete(ids: List<String>)
    suspend fun deleteOne(id: String)
    
    // 可选的自定义操作
    suspend fun customOperation(operation: String, params: Map<String, Any>): Any? {
        throw UnsupportedOperationException("Custom operation not supported")
    }
}
