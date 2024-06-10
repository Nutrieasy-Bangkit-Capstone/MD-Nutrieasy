package com.capstone.nutrieasy.ui.main.profile

import com.capstone.nutrieasy.data.api.model.User
import com.capstone.nutrieasy.data.api.model.UserDataResponse

data class ProfileFragmentViewState(
    var isLoading: Boolean,
    var isSuccess: Boolean,
    var isError: Boolean,
    var errorMessage: String,
    var data: User?
)