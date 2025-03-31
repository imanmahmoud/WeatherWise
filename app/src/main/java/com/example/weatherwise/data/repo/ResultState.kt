package com.example.weatherwise.data.repo

/*
sealed class Response< T> {
    data class Success<T>(val data: T) : Response<T>()
    data class Error(val message: String) : Response<Nothing>()
    data object Loading : Response<Nothing>()
}*/



sealed class ResultState<T>/*(val data: T? = null, val message: String? = null)*/ {
    class Loading<T> : ResultState<T>()
    data class Success<T>(val data: T) : ResultState<T>(/*data*/)
    data class Failure<T>(val message: String/*, data: T? = null*/) : ResultState<T>(/*data, message*/)
}

