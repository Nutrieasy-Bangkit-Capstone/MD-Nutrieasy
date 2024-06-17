package com.capstone.nutrieasy.data.api.model

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

data class HistoryResponse(

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("history")
	val history: List<HistoryItem>,

	@field:SerializedName("message")
	val message: String
)

@Parcelize
data class HistoryItem(

	@field:SerializedName("servingUnit")
	val servingUnit: String,

	@field:SerializedName("createdAt")
	val createdAt: String,

	@field:SerializedName("foodName")
	val foodName: String,

	@field:SerializedName("foodId")
	val foodId: Int,

	@field:SerializedName("servingQty")
	val servingQty: Int,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("nutrientsDetailList")
	val nutrientsDetailList: List<NutrientsDetailListItem>,

	@field:SerializedName("userId")
	val userId: String,

	@field:SerializedName("servingWeightGrams")
	val servingWeightGrams: Int
): Parcelable
