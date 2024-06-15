package com.capstone.nutrieasy.data.api.model

import com.google.gson.annotations.SerializedName

data class UpdateProfileResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("user")
	val user: UpdatedUser
)

data class UpdatedUser(
	@field:SerializedName("uid")
	val uid: String,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("gender")
	val gender: String? = null,

	@field:SerializedName("fullName")
	val fullName: String,

	@field:SerializedName("weight")
	val weight: Int? = null,

	@field:SerializedName("dateOfBirth")
	val dateOfBirth: String? = null,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("height")
	val height: Int? = null
)
