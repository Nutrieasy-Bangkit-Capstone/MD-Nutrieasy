package com.capstone.nutrieasy.helper

sealed class Result<T>(
    val data: T? = null,
    val message: String? = null,
    val code: Int? = null
) {
    class Success<T>(data: T): Result<T>(data = data)
    class Loading<T>: Result<T>()
    class Error<T>(message: String? = "error get data", code: Int?, data: T?): Result<T>(message = message, code = code, data = data)
}