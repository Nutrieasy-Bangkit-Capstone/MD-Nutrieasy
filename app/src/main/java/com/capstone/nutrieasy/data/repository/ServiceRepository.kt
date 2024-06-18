package com.capstone.nutrieasy.data.repository

import com.capstone.nutrieasy.data.api.AppService
import com.capstone.nutrieasy.data.api.model.HistoryItem
import com.capstone.nutrieasy.data.api.model.ItemData
import com.capstone.nutrieasy.data.api.model.TotalIntakeListItem
import com.capstone.nutrieasy.util.Result
import com.google.gson.JsonObject
import okhttp3.MultipartBody
import okhttp3.RequestBody

class ServiceRepository(
    private val appService: AppService
) {
    suspend fun scan(uid: RequestBody, image: MultipartBody.Part): Result<ItemData>{
        return try{
            val result = appService.scan(uid, image)
            if(result.success){
                Result.Success(result.data)
            }else{
                Result.Error(result.message)
            }
        }catch(exc: Exception){
            Result.Error(exc.message ?: "Failed to scan the image")
        }
    }

    suspend fun track(
        uid: String, itemName: String,
        imageUrl: String, quantity: Int
    ): Result<String>{
        return try {
            val body = JsonObject().apply {
                addProperty("uid", uid)
                addProperty("foodName", itemName)
                addProperty("imageUrl", imageUrl)
                addProperty("quantity", quantity)
            }
            val result = appService.track(body)
            Result.Success(result.message)
        }catch (exc: Exception){
            Result.Error(exc.message ?: "Failed to track, please try again")
        }
    }

    suspend fun getDailyNutrition(
        uid: String,
        date: String? = null
    ): Result<List<TotalIntakeListItem>>{
        return try {
            val result = appService.getDailyNutrition(uid, date)
            if(result.success){
                Result.Success(result.totalIntakeList)
            }else{
                Result.Error(result.message)
            }
        }catch(exc: Exception){
            Result.Error(exc.message ?: "Failed to get daily Nutrition")
        }
    }

    suspend fun getHistory(
        uid: String,
        date: String? = null
    ): Result<List<HistoryItem>>{
        return try{
            val result = appService.getHistory(uid, date)
            if(result.success){
                Result.Success(result.history)
            }else Result.Error(result.message)
        }catch(exc: Exception){
            return Result.Error(exc.message ?: "Failed to fetch history")
        }
    }
}