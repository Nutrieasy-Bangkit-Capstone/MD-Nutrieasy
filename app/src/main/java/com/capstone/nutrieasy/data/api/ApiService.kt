package com.capstone.nutrieasy.data.api

import com.capstone.nutrieasy.data.api.model.LoginResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {
    @FormUrlEncoded
    @POST("loginByGoogle")
    suspend fun login(
        @Field("email") email: String,
        @Field("fullname") fullname: String,
        @Field("uid") uid: String
    ): LoginResponse
}