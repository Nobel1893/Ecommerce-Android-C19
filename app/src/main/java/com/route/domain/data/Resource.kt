package com.route.domain.data

sealed class Resource<T> {
    data class Success<T>(val data:T? = null): Resource<T>()
    data class Error<T>(val throwable: Throwable): Resource<T>()
    class Loading<T>(): Resource<T>()
}