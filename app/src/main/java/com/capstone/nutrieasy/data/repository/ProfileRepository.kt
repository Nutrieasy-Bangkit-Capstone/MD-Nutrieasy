package com.capstone.nutrieasy.data.repository

import com.capstone.nutrieasy.data.api.AppService
import com.capstone.nutrieasy.data.api.model.User
import com.capstone.nutrieasy.util.Result

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
}