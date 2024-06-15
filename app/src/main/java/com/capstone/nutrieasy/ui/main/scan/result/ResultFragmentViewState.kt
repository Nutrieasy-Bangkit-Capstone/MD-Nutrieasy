package com.capstone.nutrieasy.ui.main.scan.result

import com.capstone.nutrieasy.data.api.model.ItemData
import com.capstone.nutrieasy.data.api.model.TotalIntakeListItem

data class ResultFragmentViewState(
    var isLoading: Boolean,
    var isTrackLoading: Boolean,
    var isDailyNutritionLoading: Boolean,
    var isDailyNutritionSuccess: Boolean,
    var isSuccess: Boolean,
    var isTrackSuccess: Boolean,
    var isError: Boolean,
    var isTrackError: Boolean,
    var isDailyNutritionError: Boolean,
    var errorMessage: String,
    var data: ItemData?,
    var dailyNutritionData: List<TotalIntakeListItem>?,
    var size: Int
)