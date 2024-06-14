package com.capstone.nutrieasy.data.response

import com.google.gson.annotations.SerializedName

data class  HistoryListResponse(
    @field:SerializedName("listFruit")
    val listFruit: List<HistoryResponse>?,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String,
)
