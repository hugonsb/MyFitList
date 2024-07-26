package com.happs.myfitlist.room

sealed class RepositoryResponse<out T> {
    data class Success<T>(val data: T) : RepositoryResponse<T>()
    data class Error(val message: String) : RepositoryResponse<Nothing>()
    data object Loading : RepositoryResponse<Nothing>()
}