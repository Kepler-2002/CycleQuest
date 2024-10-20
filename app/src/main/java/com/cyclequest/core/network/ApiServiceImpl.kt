package com.cyclequest.core.network

import okhttp3.*
import javax.inject.Inject
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import com.google.gson.Gson
import okhttp3.HttpUrl.Companion.toHttpUrlOrNull
import okhttp3.MediaType.Companion.toMediaTypeOrNull

class ApiServiceImpl @Inject constructor(
    private val okHttpClient: OkHttpClient
) : ApiService {
    private val gson = Gson()

    override suspend fun get(url: String, params: Map<String, String>): Result<String> = withContext(Dispatchers.IO) {
        try {
            val urlBuilder = url.toHttpUrlOrNull()?.newBuilder() ?: throw IllegalArgumentException("Invalid URL")
            params.forEach { (key, value) -> urlBuilder.addQueryParameter(key, value) }
            val request = Request.Builder().url(urlBuilder.build()).build()
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                Result.success(response.body?.string() ?: "")
            } else {
                Result.failure(Exception("HTTP错误: ${response.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun post(url: String, body: Any, params: Map<String, String>): Result<String> = withContext(Dispatchers.IO) {
        try {
            val urlBuilder = url.toHttpUrlOrNull()?.newBuilder() ?: throw IllegalArgumentException("Invalid URL")
            params.forEach { (key, value) -> urlBuilder.addQueryParameter(key, value) }
            val jsonBody = gson.toJson(body)
            val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonBody)
            val request = Request.Builder().url(urlBuilder.build()).post(requestBody).build()
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                Result.success(response.body?.string() ?: "")
            } else {
                Result.failure(Exception("HTTP错误: ${response.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun put(url: String, body: Any, params: Map<String, String>): Result<String> = withContext(Dispatchers.IO) {
        try {
            val urlBuilder = url.toHttpUrlOrNull()?.newBuilder() ?: throw IllegalArgumentException("Invalid URL")
            params.forEach { (key, value) -> urlBuilder.addQueryParameter(key, value) }
            val jsonBody = gson.toJson(body)
            val requestBody = RequestBody.create("application/json".toMediaTypeOrNull(), jsonBody)
            val request = Request.Builder().url(urlBuilder.build()).put(requestBody).build()
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                Result.success(response.body?.string() ?: "")
            } else {
                Result.failure(Exception("HTTP错误: ${response.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun delete(url: String, params: Map<String, String>): Result<String> = withContext(Dispatchers.IO) {
        try {
            val urlBuilder = url.toHttpUrlOrNull()?.newBuilder() ?: throw IllegalArgumentException("Invalid URL")
            params.forEach { (key, value) -> urlBuilder.addQueryParameter(key, value) }
            val request = Request.Builder().url(urlBuilder.build()).delete().build()
            val response = okHttpClient.newCall(request).execute()
            if (response.isSuccessful) {
                Result.success(response.body?.string() ?: "")
            } else {
                Result.failure(Exception("HTTP错误: ${response.code}"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
