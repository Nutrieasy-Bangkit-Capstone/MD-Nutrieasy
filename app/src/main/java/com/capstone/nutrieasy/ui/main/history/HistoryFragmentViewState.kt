package com.capstone.nutrieasy.ui.main.history

import com.capstone.nutrieasy.data.api.model.HistoryItem

data class HistoryFragmentViewState(
    var isLoading: Boolean,
    var isSuccess: Boolean,
    var isError: Boolean,
    var errorMessage: String,
    var data: List<HistoryItem>?
)