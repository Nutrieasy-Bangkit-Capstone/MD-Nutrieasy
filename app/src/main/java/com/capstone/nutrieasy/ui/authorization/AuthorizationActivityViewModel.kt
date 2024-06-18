package com.capstone.nutrieasy.ui.authorization

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
class AuthorizationActivityViewModel @Inject constructor(
    private val authRepository: AuthRepository
): ViewModel() {
    val user = authRepository.getFirebaseUser()
    private val _viewState = MutableLiveData(
        AuthorizationActivityViewState(
            isLoading = false,
            isSuccess = false,
            isError = false,
            errorMessage = ""
        )
    )
    val viewState: LiveData<AuthorizationActivityViewState> = _viewState

    fun refreshAccessToken(){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isLoading = true,
                isSuccess = false,
                isError = false
            )

            if(user != null){
                val result = authRepository.getToken(
                    user.email!!,
                    user.displayName ?: user.email!!,
                    user.uid
                )

                when(result){
                    is Result.Error -> {
                        _viewState.value = _viewState.value?.copy(
                            isLoading = false,
                            isError = false,
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
}