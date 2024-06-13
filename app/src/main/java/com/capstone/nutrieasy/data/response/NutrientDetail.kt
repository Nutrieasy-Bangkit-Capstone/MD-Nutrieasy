package com.capstone.nutrieasy.data.response

import com.google.gson.annotations.SerializedName

data class NutrientDetail(
    @field:SerializedName("name")
    val name: String? = null,

    @field:SerializedName("value")
    val value: Double? = null
)
