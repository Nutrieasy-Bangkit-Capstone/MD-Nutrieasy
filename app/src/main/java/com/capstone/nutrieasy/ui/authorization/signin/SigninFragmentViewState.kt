package com.capstone.nutrieasy.ui.authorization.signin

data class SigninFragmentViewState(
    var isLoading: Boolean,
    var isGoogleLoading: Boolean,
    var isSuccess: Boolean,
    var isError: Boolean,
    var errorMessage: String
)
