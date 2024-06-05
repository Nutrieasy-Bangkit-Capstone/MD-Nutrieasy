package com.capstone.nutrieasy.ui.authorization.signup

import com.google.firebase.auth.FirebaseUser

data class SignupFragmentViewState(
    var isLoading: Boolean,
    var isSuccess: Boolean,
    var isError: Boolean,
    var errorMessage: String,
    var data: FirebaseUser?
)
