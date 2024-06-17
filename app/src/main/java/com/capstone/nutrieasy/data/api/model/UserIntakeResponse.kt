package com.capstone.nutrieasy.data.api.model

import com.google.gson.annotations.SerializedName

data class UserIntakeResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("totalIntakeList")
	val totalIntakeList: List<TotalIntakeListItem>,

	@field:SerializedName("message")
	val message: String
)

data class TotalIntakeListItem(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("attrId")
	val attrId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("value")
	val value: Double,

	@field:SerializedName("minValue")
	val minValue: Double,

	@field:SerializedName("maxValue")
	val maxValue: Double
)
