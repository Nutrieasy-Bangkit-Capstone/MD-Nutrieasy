package com.capstone.nutrieasy.data.repository

import com.capstone.nutrieasy.data.api.AppService
import com.capstone.nutrieasy.data.api.model.ItemData
import com.capstone.nutrieasy.util.Result
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ServiceRepository(
    private val appService: AppService
) {
    suspend fun scan(uid: RequestBody, image: MultipartBody.Part): Result<ItemData>{
        return try{
            val result = appService.scan(uid, image)
            Result.Success(result.data)
        }catch(exc: Exception){
            Result.Error(exc.message ?: "Failed to scan the image")
        }
    }
}