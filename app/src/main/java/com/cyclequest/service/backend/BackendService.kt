package com.cyclequest.service.backend

import com.cyclequest.core.base.BaseEntity
import com.cyclequest.core.di.BackendRetrofit
import com.cyclequest.core.network.ApiError
import com.cyclequest.core.network.ApiResult
import com.cyclequest.core.network.NetworkConfig
import org.xml.sax.ErrorHandler
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.http.*
import javax.inject.Inject

class BackendService @Inject constructor(
    @BackendRetrofit private val retrofit: Retrofit,
    private val networkConfig: NetworkConfig
) {
    suspend fun <T : Any> get(endpoint: String, params: Map<String, String> = emptyMap()): ApiResult<T> {
        return executeRequest {
            api.get<T>(buildUrl(endpoint), params)
        }
    }

    suspend fun <T : Any> post(endpoint: String, body: Any): ApiResult<T> {
        return executeRequest {
            api.post<T>(buildUrl(endpoint), body)
        }
    }

    suspend fun <T : Any> put(endpoint: String, body: Any): ApiResult<T> {
        return executeRequest {
            api.put<T>(buildUrl(endpoint), body)
        }
    }

    suspend fun delete(endpoint: String): ApiResult<Unit> {
        return executeRequest {
            api.delete(buildUrl(endpoint))
        }
    }

    private fun buildUrl(endpoint: String): String {
        return "${networkConfig.baseUrl.trimEnd('/')}/$endpoint".trimEnd('/')
    }

    private suspend fun <T> executeRequest(request: suspend () -> Response<T>): ApiResult<T> {
        return try {
            val response = request()
            when {
                response.isSuccessful -> {
                    response.body()?.let { 
                        ApiResult.Success(it)
                    } ?: ApiResult.Error(ApiError.ServerError("Empty response body"))
                }
                else -> ApiResult.Error(ApiError.HttpError(response.code(), response.message()))
            }
        } catch (e: Exception) {
            ApiResult.Error(ApiError.NetworkError(e))
        }
    }

    private val api = retrofit.create(BackendApi::class.java)
}

private interface BackendApi {
    @GET
    suspend fun <T : Any> get(
        @Url url: String,
        @QueryMap params: Map<String, String> = emptyMap()
    ): Response<T>

    @POST
    suspend fun <T : Any> post(
        @Url url: String,
        @Body body: Any
    ): Response<T>

    @PUT
    suspend fun <T : Any> put(
        @Url url: String,
        @Body body: Any
    ): Response<T>

    @DELETE
    suspend fun delete(@Url url: String): Response<Unit>
}