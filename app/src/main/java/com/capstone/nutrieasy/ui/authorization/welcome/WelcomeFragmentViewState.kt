package com.capstone.nutrieasy.ui.authorization.welcome

import com.google.firebase.auth.FirebaseUser

data class WelcomeFragmentViewState(
    var isLoading: Boolean,
    var isSuccess: Boolean,
    var isError: Boolean,
    var errorMessage: String,
    var user: FirebaseUser?
)