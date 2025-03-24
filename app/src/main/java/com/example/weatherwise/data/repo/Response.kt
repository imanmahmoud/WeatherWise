package com.example.weatherwise.data.repo

/*
sealed class Response< T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Error(val message: String) : Response<Nothing>()
    data object Loading : Response<Nothing>()
}*/



sealed class Response<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Response<T>(data)
    class Loading<T> : Response<T>()
    class Error<T>(message: String, data: T? = null) : Response<T>(data, message)
}

