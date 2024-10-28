package com.cyclequest.service.backend

import com.cyclequest.core.base.BaseEntity
import retrofit2.Retrofit
import javax.inject.Inject

class BackendService @Inject constructor(
    private val retrofit: Retrofit
) {
    interface Api {
        suspend fun <T : BaseEntity> getAll(path: String, entityClass: Class<T>): List<T>
        suspend fun <T : BaseEntity> getById(path: String, id: String, entityClass: Class<T>): T?
        suspend fun <T : BaseEntity> getByIds(path: String, ids: List<String>, entityClass: Class<T>): List<T>
        suspend fun <T : BaseEntity> batchUpdate(path: String, entities: List<T>, entityClass: Class<T>): List<T>
        suspend fun <T : BaseEntity> update(path: String, entity: T, entityClass: Class<T>): T
        suspend fun batchDelete(path: String, ids: List<String>)
        suspend fun delete(path: String, id: String)
    }

    fun getApi(): Api = retrofit.create(Api::class.java)

    suspend fun <T> request(call: suspend () -> T): T {
        return try {
            call()
        } catch (e: Exception) {
            // 统一错误处理
            throw e
        }
    }
}