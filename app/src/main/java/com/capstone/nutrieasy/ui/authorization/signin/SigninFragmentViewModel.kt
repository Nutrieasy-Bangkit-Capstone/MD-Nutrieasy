package com.capstone.nutrieasy.ui.authorization.signin

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrieasy.data.repository.AuthRepository
import com.capstone.nutrieasy.data.repository.SessionRepository
import com.capstone.nutrieasy.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SigninFragmentViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository
): ViewModel() {
    var email: String = ""
    var password: String = ""

    private val _viewState = MutableLiveData(
        SigninFragmentViewState(
            isLoading = false,
            isGoogleLoading = false,
            isSuccess = false,
            isError = false,
            errorMessage = ""
        )
    )
    val viewState: LiveData<SigninFragmentViewState> = _viewState

    fun signin(){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isLoading = true,
                isSuccess = false,
                isError = false
            )

            when(val result = authRepository.firebaseSignin(email, password)){
                is Result.Error -> {
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = result.message
                    )
                }
                is Result.Success -> {
                    val tokenResult = authRepository.getToken(email, result.data.displayName ?: email, result.data.uid)
                    when(tokenResult){
                        is Result.Error -> {
                            _viewState.value = _viewState.value?.copy(
                                isLoading = false,
                                isError = true,
                                errorMessage = tokenResult.message
                            )
                        }
                        is Result.Success -> {
                            Log.d("Token Testing", tokenResult.data)
                            sessionRepository.saveSession(tokenResult.data)
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

    fun signinWithGoogle(token: String){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isGoogleLoading = true,
                isSuccess = false,
                isError = false
            )
            when(
                val result = authRepository.firebaseAuthWithGoogle(token)
            ){
                is Result.Error -> {
                    _viewState.value = _viewState.value?.copy(
                        isGoogleLoading = false,
                        isSuccess = false,
                        isError = true,
                        errorMessage = result.message
                    )
                }
                is Result.Success -> {
                    val tokenResult = authRepository.getToken(email, result.data.displayName ?: email, result.data.uid)
                    when(tokenResult){
                        is Result.Error -> {
                            _viewState.value = _viewState.value?.copy(
                                isLoading = false,
                                isError = true,
                                errorMessage = tokenResult.message
                            )
                        }
                        is Result.Success -> {
                            email = ""
                            password = ""
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
}