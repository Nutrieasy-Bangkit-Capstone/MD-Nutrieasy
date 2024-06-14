package com.capstone.nutrieasy.data.response

import com.google.gson.annotations.SerializedName

data class HistoryResponse(
    @field:SerializedName("imageUrl")
    val photoUrl: String? = null,

    @field:SerializedName("createdAt")
    val createdAt: String? = null,

    @field:SerializedName("foodName")
    val name: String? = null,

    @field:SerializedName("nutrientsDetailList")
    val nutrientsDetailList: List<NutrientDetail>? = null,

    @field:SerializedName("id")
    val id: String? = null
)
