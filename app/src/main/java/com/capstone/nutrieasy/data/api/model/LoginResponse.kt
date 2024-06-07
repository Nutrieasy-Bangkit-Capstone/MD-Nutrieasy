package com.capstone.nutrieasy.data.api.model

import com.google.gson.annotations.SerializedName

data class LoginResponse(
    @field:SerializedName("success")
    val success: Boolean,
    @field:SerializedName("message")
    val message: String,
    @field:SerializedName("token")
    val token: String
)
