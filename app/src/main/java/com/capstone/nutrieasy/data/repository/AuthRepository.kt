package com.capstone.nutrieasy.data.repository

import com.capstone.nutrieasy.data.api.AuthService
import com.capstone.nutrieasy.data.response.ErrorResponse
import com.capstone.nutrieasy.util.Result
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.userProfileChangeRequest
import com.google.gson.Gson
import com.google.gson.JsonObject
import kotlinx.coroutines.tasks.await
import retrofit2.HttpException

class AuthRepository(
    private val auth: FirebaseAuth,
    private val apiService: AuthService
) {
    suspend fun firebaseAuthWithGoogle(idToken: String): Result<FirebaseUser> {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        if(result.user == null){
            return Result.Error("Sign in failed")
        }
        return Result.Success(result.user!!)
    }

    suspend fun firebaseSignup(email: String, password: String): Result<FirebaseUser> {
        try {
            val result = auth.createUserWithEmailAndPassword(email, password).await()
            if(result.user == null){
                return Result.Error("Sign up failed")
            }
            return Result.Success(result.user!!)
        }catch(exc: Exception){
            return Result.Error(exc.message ?: "Sign up failed")
        }
    }

    suspend fun firebaseSignin(email: String, password: String): Result<FirebaseUser> {
        try{
            val result = auth.signInWithEmailAndPassword(email, password).await()
            if(result.user == null){
                return Result.Error("Sign in failed")
            }
            return Result.Success(result.user!!)
        }catch(exc: Exception){
            return Result.Error(exc.message ?: "Sign in failed")
        }
    }

    fun getFirebaseUser(): FirebaseUser?{
        return auth.currentUser
    }

    suspend fun updateUserDisplayName(user: FirebaseUser, name: String): Result<String>{
        return try{
            user.updateProfile(
                userProfileChangeRequest {
                    displayName = name
                }
            ).await()
            Result.Success("Display name changed successfully")
        }catch(exc: Exception){
            Result.Error(exc.message ?: "Failed to change display name")
        }
    }

    suspend fun getToken(email: String, fullname: String, uid: String): Result<String>{
        return try{
            val body = JsonObject().apply {
                addProperty("email", email)
                addProperty("fullName", fullname)
                addProperty("uid", uid)
            }
            val result = apiService.login(body)
            Result.Success(result.token)
        }catch (exc: Exception){
            Result.Error(exc.message ?: "Failed to get token")
        }
    }

    fun logout(){
        auth.signOut()
    }
}