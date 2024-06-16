package com.capstone.nutrieasy.ui.main.history

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrieasy.data.repository.AuthRepository
import com.capstone.nutrieasy.data.repository.ServiceRepository
import com.capstone.nutrieasy.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import javax.inject.Inject

@HiltViewModel
class HistoryFragmentViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val serviceRepository: ServiceRepository
): ViewModel() {
    var date: String = ""
    var long: Long = 0L
    private val _viewState = MutableLiveData(
        HistoryFragmentViewState(
            isLoading = false,
            isSuccess = false,
            isError = false,
            errorMessage = "",
            data = null
        )
    )
    val viewState: LiveData<HistoryFragmentViewState> = _viewState

    fun getHistory(){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isLoading = true,
                isSuccess = false,
                isError = false
            )
            val user = authRepository.getFirebaseUser()!!
            val result = if(date.isEmpty()){
                serviceRepository.getHistory(user.uid)
            }else {
                val dateFormat = SimpleDateFormat("yyyy-MM-dd", Locale.US)
                val newDate = dateFormat.format(Date(long))
                serviceRepository.getHistory(user.uid, newDate)
            }

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
                        isSuccess = true,
                        data = result.data
                    )
                }
            }
        }
    }
}