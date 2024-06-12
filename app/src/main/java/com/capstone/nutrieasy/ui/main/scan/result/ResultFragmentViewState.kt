package com.capstone.nutrieasy.ui.main.scan.result

import com.capstone.nutrieasy.data.api.model.ItemData

data class ResultFragmentViewState(
    var isLoading: Boolean,
    var isTrackLoading: Boolean,
    var isSuccess: Boolean,
    var isTrackSuccess: Boolean,
    var isError: Boolean,
    var isTrackError: Boolean,
    var errorMessage: String,
    var data: ItemData?,
    var size: Int
)