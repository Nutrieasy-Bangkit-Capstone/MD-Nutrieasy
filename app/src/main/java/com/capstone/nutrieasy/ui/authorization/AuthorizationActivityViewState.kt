package com.capstone.nutrieasy.ui.authorization

data class AuthorizationActivityViewState(
    var isLoading: Boolean,
    var isSuccess: Boolean,
    var isError: Boolean,
    var errorMessage: String
)