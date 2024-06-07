package com.capstone.nutrieasy.ui.main.profile

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrieasy.data.repository.AuthRepository
import com.capstone.nutrieasy.data.repository.SessionRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProfileFragmentViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val sessionRepository: SessionRepository
): ViewModel() {
    private val _viewState = MutableLiveData(
        ProfileFragmentViewState(
            isLoading = false,
            isSuccess = false,
            isError = false,
            errorMessage = ""
        )
    )
    val viewState: LiveData<ProfileFragmentViewState> = _viewState

    fun logout(){
        viewModelScope.launch {
            authRepository.logout()
            sessionRepository.logout()
        }
    }
}