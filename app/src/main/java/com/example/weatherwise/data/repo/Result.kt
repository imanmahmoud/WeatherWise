package com.example.weatherwise.data.repo

/*
sealed class Response< T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Error(val message: String) : Response<Nothing>()
    data object Loading : Response<Nothing>()
}*/



sealed class Result<T>/*(val data: T? = null, val message: String? = null)*/ {
    class Loading<T> : Result<T>()
    data class Success<T>(val data: T) : Result<T>(/*data*/)
    data class Failure<T>(val message: String/*, data: T? = null*/) : Result<T>(/*data, message*/)
}

