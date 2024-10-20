package com.cyclequest.core.network

interface ApiService {
    suspend fun get(url: String, params: Map<String, String> = emptyMap()): Result<String>
    suspend fun post(url: String, body: Any, params: Map<String, String> = emptyMap()): Result<String>
    suspend fun put(url: String, body: Any, params: Map<String, String> = emptyMap()): Result<String>
    suspend fun delete(url: String, params: Map<String, String> = emptyMap()): Result<String>
}


