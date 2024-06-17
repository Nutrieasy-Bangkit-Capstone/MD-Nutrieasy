package com.capstone.nutrieasy.ui.main.userhome

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrieasy.data.repository.AuthRepository
import com.capstone.nutrieasy.data.repository.ServiceRepository
import com.capstone.nutrieasy.util.Result
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class UserHomeFragmentViewModel @Inject constructor(
    private val authRepository: AuthRepository,
    private val serviceRepository: ServiceRepository
): ViewModel() {
    val user: FirebaseUser = authRepository.getFirebaseUser()!!
    private val _viewState = MutableLiveData(
        UserHomeFragmentViewState(
            isDailyNutritionLoading = false,
            isHistoryLoading = false,
            isDailyNutritionSuccess = false,
            isHistorySuccess = false,
            isDailyNutritionError = false,
            isHistoryError = false,
            errorMessage = "",
            dailyNutritionData = null,
            historyData = null
        )
    )
    val viewState: LiveData<UserHomeFragmentViewState> = _viewState
    private val nutritionName = listOf("energy", "sugar", "fiber", "protein")

    fun getDailyNutrition(){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isDailyNutritionLoading = true,
                isDailyNutritionError = false,
                isDailyNutritionSuccess = false,
            )

            when(val result = serviceRepository.getDailyNutrition(user.uid)){
                is Result.Error -> {
                    _viewState.value = _viewState.value?.copy(
                        isDailyNutritionLoading = false,
                        isDailyNutritionError = true,
                        errorMessage = result.message
                    )
                }
                is Result.Success -> {
                    val nutrition = result.data.filter { item ->
                        var isExist = false
                        for(name in nutritionName){
                            if(!isExist){
                                isExist = item.name.contains(name, true)
                            }else break
                        }
                        isExist
                    }
                    Log.d("Daftar Nutrisi", "$nutrition")
                    _viewState.value = _viewState.value?.copy(
                        isDailyNutritionLoading = false,
                        isDailyNutritionSuccess = true,
                        dailyNutritionData = nutrition
                    )
                }
            }
        }
    }

    fun getHistory(){
        viewModelScope.launch{
            _viewState.value = _viewState.value?.copy(
                isHistoryLoading = true,
                isHistoryError = false,
                isHistorySuccess = false
            )

            when(val result = serviceRepository.getHistory(user.uid)){
                is Result.Error -> {
                    _viewState.value = _viewState.value?.copy(
                        isHistoryLoading = false,
                        isHistoryError = true,
                        errorMessage = result.message
                    )
                }
                is Result.Success -> {
                    _viewState.value = _viewState.value?.copy(
                        isHistoryLoading = false,
                        isHistorySuccess = true,
                        historyData = result.data
                    )
                }
            }
        }
    }
}