package com.capstone.nutrieasy.ui.main.editprofile

import com.capstone.nutrieasy.data.api.model.User

data class EditProfileViewState(
    var isLoading: Boolean,
    var isSuccess: Boolean,
    var isError: Boolean,
    var errorMessage: String
)