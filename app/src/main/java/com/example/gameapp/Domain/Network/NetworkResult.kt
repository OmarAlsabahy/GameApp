package com.example.gameapp.Domain.Network

sealed class NetworkResult<T>(val data: T? = null, val error: String? = null) {
    class Initial<T> : NetworkResult<T>()
    class Success<T>(data: T) : NetworkResult<T>(data)
    class Error<T>(error: String, data: T? = null) : NetworkResult<T>(data, error)
    class Loading<T> : NetworkResult<T>()
}