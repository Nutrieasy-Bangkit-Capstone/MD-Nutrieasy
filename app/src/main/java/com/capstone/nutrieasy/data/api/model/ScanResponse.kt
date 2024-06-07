package com.capstone.nutrieasy.data.api.model

import com.google.gson.annotations.SerializedName

data class ScanResponse(

	@field:SerializedName("data")
	val data: ItemData,

	@field:SerializedName("success")
	val success: Boolean,

	@field:SerializedName("message")
	val message: String
)

data class NutrientsDetailListItem(

	@field:SerializedName("unit")
	val unit: String,

	@field:SerializedName("attrId")
	val attrId: Int,

	@field:SerializedName("name")
	val name: String,

	@field:SerializedName("value")
	val value: Any
)

data class ItemData(

	@field:SerializedName("servingUnit")
	val servingUnit: String,

	@field:SerializedName("foodName")
	val foodName: String,

	@field:SerializedName("imageUrl")
	val imageUrl: String,

	@field:SerializedName("servingQty")
	val servingQty: Int,

	@field:SerializedName("nutrientsDetailList")
	val nutrientsDetailList: List<NutrientsDetailListItem>,

	@field:SerializedName("servingWeightGrams")
	val servingWeightGrams: Int
)
