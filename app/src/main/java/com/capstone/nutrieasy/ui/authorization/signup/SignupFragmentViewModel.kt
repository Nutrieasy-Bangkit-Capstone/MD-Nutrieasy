package com.capstone.nutrieasy.ui.authorization.signup

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
class SignupFragmentViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository
) : ViewModel() {
    var name: String = ""
    var email: String = ""
    var password: String = ""

    private val _viewState = MutableLiveData(
        SignupFragmentViewState(
            isLoading = false,
            isSuccess = false,
            isError = false,
            errorMessage = "",
            data = null
        )
    )
    val viewState: LiveData<SignupFragmentViewState> = _viewState

    fun signup(){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isLoading = true,
                isSuccess = false,
                isError = false
            )
            when(val result = authRepository.firebaseSignup(email, password)){
                is Result.Error -> {
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = result.message
                    )
                }
                is Result.Success -> {
                    val nameResult = authRepository.updateUserDisplayName(result.data, name)
                    when(nameResult){
                        is Result.Error -> {
                            result.data.delete()
                            _viewState.value = _viewState.value?.copy(
                                isLoading = false,
                                isError = true,
                                errorMessage = nameResult.message
                            )
                        }
                        is Result.Success -> {
                            val tokenResult = authRepository.getToken(email, name, result.data.uid)
                            when(tokenResult){
                                is Result.Error -> {
                                    _viewState.value = _viewState.value?.copy(
                                        isLoading = false,
                                        isError = true,
                                        errorMessage = tokenResult.message
                                    )
                                }
                                is Result.Success -> {
                                    sessionRepository.saveSession(tokenResult.data)
                                    _viewState.value = _viewState.value?.copy(
                                        isLoading = false,
                                        isSuccess = true,
                                        data = result.data
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}