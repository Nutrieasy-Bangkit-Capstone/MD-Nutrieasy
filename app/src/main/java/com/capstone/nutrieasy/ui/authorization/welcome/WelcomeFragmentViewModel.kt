package com.capstone.nutrieasy.ui.authorization.welcome

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrieasy.data.repository.AuthRepository
import com.capstone.nutrieasy.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class WelcomeFragmentViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel(){
    private val _viewState = MutableLiveData(
        WelcomeFragmentViewState(
            isLoading = false,
            isSuccess = false,
            isError = false,
            errorMessage = "",
            user = null
        )
    )
    val viewState: LiveData<WelcomeFragmentViewState> = _viewState

    fun signinWithGoogle(token: String){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isLoading = true,
                isSuccess = false,
                isError = false
            )
            when(
                val result = authRepository.firebaseAuthWithGoogle(token)
            ){
                is Result.Error -> {
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false,
                        isSuccess = false,
                        isError = true,
                        errorMessage = result.message
                    )
                }
                is Result.Success -> {
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false,
                        isSuccess = true,
                        isError = false,
                        user = result.data
                    )
                }
            }
        }
    }
}