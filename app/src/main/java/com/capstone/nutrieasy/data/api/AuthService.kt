package com.capstone.nutrieasy.data.api

import com.capstone.nutrieasy.data.api.model.LoginResponse
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface AuthService {
    @POST("loginByGoogle")
    suspend fun login(@Body body: JsonObject): LoginResponse
}