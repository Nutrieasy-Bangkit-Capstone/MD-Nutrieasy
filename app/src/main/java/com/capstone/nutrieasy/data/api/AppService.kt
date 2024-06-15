package com.capstone.nutrieasy.data.api

import com.capstone.nutrieasy.data.api.model.HistoryResponse
import com.capstone.nutrieasy.data.api.model.ScanResponse
import com.capstone.nutrieasy.data.api.model.TrackResponse
import com.capstone.nutrieasy.data.api.model.UpdateProfileResponse
import com.capstone.nutrieasy.data.api.model.UserDataResponse
import com.capstone.nutrieasy.data.api.model.UserIntakeResponse
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
import retrofit2.http.PUT
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

    @PUT("user/updateProfile")
    suspend fun updateProfile(
        @Body body: JsonObject
    ): UpdateProfileResponse

    @GET("intake")
    suspend fun getDailyNutrition(
        @Query("uid") uid: String,
        @Query("date") date: String?,
    ): UserIntakeResponse

    @GET("user/history")
    suspend fun getUserHistory(
        @Query("uid") uid: String
    ): HistoryListResponse

    @GET("user/history")
    suspend fun getHistory(
        @Query("uid") uid: String,
        @Query("date") date: String?
    ): HistoryResponse
//    suspend fun getUserHistory(@Query("uid") uid: String): HistoryListResponse

    @GET("stories/{id}")
    fun getHistoryDetail(@Query("uid") uid: String, @Path("id") id: String): Call<HistoryDetailResponse>
}