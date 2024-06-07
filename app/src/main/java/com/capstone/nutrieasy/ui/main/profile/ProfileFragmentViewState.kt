package com.capstone.nutrieasy.ui.main.profile

data class ProfileFragmentViewState(
    var isLoading: Boolean,
    var isSuccess: Boolean,
    var isError: Boolean,
    var errorMessage: String,
)