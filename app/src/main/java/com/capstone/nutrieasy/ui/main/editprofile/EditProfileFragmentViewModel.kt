package com.capstone.nutrieasy.ui.main.editprofile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrieasy.data.repository.AuthRepository
import com.capstone.nutrieasy.data.repository.ProfileRepository
import com.capstone.nutrieasy.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class EditProfileFragmentViewModel @Inject constructor(
    private val profileRepository: ProfileRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    var displayName = ""
    var bod = ""
    var gender = ""
    var height = 0
    var weight = 0

    private val _viewState = MutableLiveData(
        EditProfileViewState(
            isLoading = false,
            isSuccess = false,
            isError = false,
            errorMessage = ""
        )
    )
    val viewState: LiveData<EditProfileViewState> = _viewState

    fun updateProfile(){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isLoading = true,
                isSuccess = false,
                isError = false
            )
            val user = authRepository.getFirebaseUser()
            val result = profileRepository.updateProfile(
                user!!.uid, displayName,
                bod, weight, height, gender
            )

            when(result){
                is Result.Error -> {
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = result.message
                    )
                }
                is Result.Success -> {
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false,
                        isSuccess = true
                    )
                }
            }
        }
    }
}