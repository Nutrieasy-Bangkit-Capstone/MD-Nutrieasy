package com.capstone.nutrieasy.ui.main.userhome

import com.capstone.nutrieasy.data.api.model.HistoryItem
import com.capstone.nutrieasy.data.api.model.TotalIntakeListItem

data class UserHomeFragmentViewState(
    var isDailyNutritionLoading: Boolean,
    var isHistoryLoading: Boolean,
    var isDailyNutritionSuccess: Boolean,
    var isHistorySuccess: Boolean,
    var isHistoryError: Boolean,
    var isDailyNutritionError: Boolean,
    var errorMessage: String,
    var dailyNutritionData: List<TotalIntakeListItem>?,
    var historyData: List<HistoryItem>?
)
