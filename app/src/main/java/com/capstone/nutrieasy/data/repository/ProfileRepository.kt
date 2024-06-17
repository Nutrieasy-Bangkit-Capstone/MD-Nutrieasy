package com.capstone.nutrieasy.data.repository

import com.capstone.nutrieasy.data.api.AppService
import com.capstone.nutrieasy.data.api.model.User
import com.capstone.nutrieasy.util.Result
import com.google.gson.JsonObject

class ProfileRepository(
    private val appService: AppService
) {
    suspend fun getProfile(uid: String): Result<User>{
        return try{
            val result = appService.getProfile(uid)
            Result.Success(result.user)
        }catch(exc: Exception){
            Result.Error(exc.message ?: "Failed to get user profile")
        }
    }

    suspend fun updateProfile(
        uid: String,
        displayName: String, bod: String? = null,
        weight: Int, height: Int, activityLevel: String? = null,
        gender: String? = null
    ): Result<String> {
        val body = JsonObject().apply {
            addProperty("uid", uid)
            addProperty("fullName", displayName)
            addProperty("gender", gender)
            addProperty("dateOfBirth", bod)
            addProperty("height", height)
            addProperty("weight", weight)
            addProperty("activityLevel", activityLevel)
        }
        return try{
            val result = appService.updateProfile(body)
            Result.Success("Update profile success")
        }catch(exc: Exception){
            Result.Error(exc.message ?: "Failed to update profile")
        }
    }
}