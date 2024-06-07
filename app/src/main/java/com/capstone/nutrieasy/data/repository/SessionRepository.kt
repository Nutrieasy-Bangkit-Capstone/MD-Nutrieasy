package com.capstone.nutrieasy.data.repository

import com.capstone.nutrieasy.data.local.pref.TokenModel
import com.capstone.nutrieasy.data.local.pref.TokenPreference
import kotlinx.coroutines.flow.Flow

class SessionRepository (
    private val tokenPreference: TokenPreference
) {
    suspend fun saveSession(token: String){
        tokenPreference.saveToken(token)
    }

    fun getSession(): Flow<String> {
        return tokenPreference.getSession()
    }

    suspend fun logout(){
        tokenPreference.logout()
    }
}