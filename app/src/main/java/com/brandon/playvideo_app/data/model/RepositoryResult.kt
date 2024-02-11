package com.brandon.playvideo_app.data.model

sealed class RepositoryResult<out T> {
    data class Success<out T>(val data: T) : RepositoryResult<T>()
    data class Error(val code: Int, val message: String) : RepositoryResult<Nothing>()
}