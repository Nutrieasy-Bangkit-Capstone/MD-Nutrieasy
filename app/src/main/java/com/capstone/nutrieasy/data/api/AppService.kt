package com.capstone.nutrieasy.data.api

import com.capstone.nutrieasy.data.api.model.ScanResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface AppService {
    @Multipart
    @POST("scan")
    suspend fun scan(
        @Part("uid") uid: RequestBody,
        @Part image: MultipartBody.Part
    ): ScanResponse
}