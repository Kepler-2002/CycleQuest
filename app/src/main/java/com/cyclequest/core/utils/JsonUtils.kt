package com.cyclequest.core.utils

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import javax.inject.Inject
import javax.inject.Singleton

// core/utils/JsonUtils.kt
@Singleton
class JsonUtils @Inject constructor(
    val gson: Gson
) {
    fun <T> fromJson(json: String, classOfT: Class<T>): T {
        return gson.fromJson(json, classOfT)
    }

    fun <T> toJson(obj: T): String {
        return gson.toJson(obj)
    }

    // 处理集合类型的JSON
    inline fun <reified T> fromJsonArray(json: String): List<T> {
        val type = object : TypeToken<List<T>>() {}.type
        return gson.fromJson(json, type)
    }

    // 安全的JSON解析
    fun <T> parseJsonSafely(json: String, classOfT: Class<T>): Result<T> {
        return try {
            Result.success(gson.fromJson(json, classOfT))
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}