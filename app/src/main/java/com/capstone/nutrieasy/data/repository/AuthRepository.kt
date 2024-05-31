package com.capstone.nutrieasy.data.repository

import com.capstone.nutrieasy.util.Result
import com.google.firebase.auth.AuthCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import kotlinx.coroutines.tasks.await

class AuthRepository(
    private val auth: FirebaseAuth
) {
    suspend fun firebaseAuthWithGoogle(idToken: String): Result<FirebaseUser> {
        val credential: AuthCredential = GoogleAuthProvider.getCredential(idToken, null)
        val result = auth.signInWithCredential(credential).await()
        if(result.user == null){
            return Result.Error("Sign in failed")
        }
        return Result.Success(result.user!!)
    }
}