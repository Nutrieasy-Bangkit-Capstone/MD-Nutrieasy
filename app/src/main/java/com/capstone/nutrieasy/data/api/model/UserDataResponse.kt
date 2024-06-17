package com.capstone.nutrieasy.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class UserDataResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user: User
)

@Parcelize
data class User(

	@field:SerializedName("uid")
	val uid: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("gender")
	val gender: String?,

	@field:SerializedName("fullName")
	val fullName: String,

	@field:SerializedName("weight")
	val weight: Int?,

	@field:SerializedName("dateOfBirth")
	val dateOfBirth: String?,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("height")
	val height: Int?,

	@field:SerializedName("activityLevel")
	val activityLevel: String?
): Parcelable
