package com.capstone.nutrieasy.ui.main.scan.result

import com.capstone.nutrieasy.data.api.model.ItemData

data class ResultFragmentViewState(
    var isLoading: Boolean,
    var isSuccess: Boolean,
    var isError: Boolean,
    var errorMessage: String,
    var data: ItemData?,
    var size: Int
)