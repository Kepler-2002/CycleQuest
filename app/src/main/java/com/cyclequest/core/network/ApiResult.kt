package com.cyclequest.core.network

import com.google.android.gms.common.api.Api

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val error: ApiError) : ApiResult<Nothing>()

    object Loading : ApiResult<Nothing>()
}

sealed class ApiError {
    data class HttpError(val code: Int, val message: String) : ApiError()
    data class NetworkError(val throwable: Throwable) : ApiError()
    data class ServerError(val message: String) : ApiError()
}
