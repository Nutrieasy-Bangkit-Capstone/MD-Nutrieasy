package com.capstone.nutrieasy.data.response

import com.google.gson.annotations.SerializedName

data class HistoryDetailResponse(
    @field:SerializedName("error")
    val error: Boolean? = null,

    @field:SerializedName("message")
    val message: String,

    @field:SerializedName("history")
    val story: HistoryResponse? = null
)
