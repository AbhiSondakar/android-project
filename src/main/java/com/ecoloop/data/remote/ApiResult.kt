package com.ecoloop.data.remote

sealed class ApiResult<out T> {
    data class Success<T>(val data: T) : ApiResult<T>()
    data class Error(val code: Int, val message: String) : ApiResult<Nothing>()
    data class Exception(val exception: Throwable) : ApiResult<Nothing>() {
        override fun toString(): String = "Exception: ${exception.message}"
    }

    companion object {
        fun <T> success(data: T): ApiResult<T> = Success(data)
        fun error(code: Int, message: String): ApiResult<Nothing> = Error(code, message)
        fun exception(exception: Throwable): ApiResult<Nothing> = Exception(exception)
    }
}

sealed class UiState<out T> {
    object Loading : UiState<Nothing>()
    data class Success<T>(val data: T) : UiState<T>()
    data class Error(val message: String) : UiState<Nothing>()
}
