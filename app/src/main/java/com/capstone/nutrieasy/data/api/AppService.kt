package com.capstone.nutrieasy.data.api

import com.capstone.nutrieasy.data.api.model.ScanResponse
import com.capstone.nutrieasy.data.api.model.TrackResponse
import com.capstone.nutrieasy.data.api.model.UserDataResponse
import com.capstone.nutrieasy.data.response.HistoryDetailResponse
import com.capstone.nutrieasy.data.response.HistoryListResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import com.google.gson.JsonObject
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Path
import retrofit2.http.Query

interface AppService {
    @Multipart
    @POST("scan")
    suspend fun scan(
        @Part("uid") uid: RequestBody,
        @Part image: MultipartBody.Part
    ): ScanResponse

    @POST("scan/track")
    suspend fun track(@Body body: JsonObject): TrackResponse

    @GET("user")
    suspend fun getProfile(@Query("uid") uid: String): UserDataResponse

    @GET("user/history")
    suspend fun getUserHistory(@Query("uid") uid: String): HistoryListResponse
//    suspend fun getUserHistory(@Query("uid") uid: String): HistoryListResponse

    @GET("stories/{id}")
    fun getHistoryDetail(@Query("uid") uid: String, @Path("id") id: String): Call<HistoryDetailResponse>

}