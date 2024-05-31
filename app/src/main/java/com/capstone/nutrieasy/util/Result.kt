package com.capstone.nutrieasy.util

sealed class Result<out T> {
    data class Error(val message: String): Result<Nothing>()
    data class Success<T>(val data: T): Result<T>()
}