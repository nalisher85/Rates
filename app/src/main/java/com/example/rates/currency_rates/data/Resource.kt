package com.example.rates.currency_rates.data

sealed class Resource<T> {
    class Loading<T>: Resource<T>()
    class Error<T>(val code: String, val message: String): Resource<T>()
    class Success<T>(val data: T): Resource<T>()
}