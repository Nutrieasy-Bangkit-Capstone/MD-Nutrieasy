package com.capstone.nutrieasy.ui.main.scan.result

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrieasy.data.repository.AuthRepository
import com.capstone.nutrieasy.data.repository.ServiceRepository
import com.capstone.nutrieasy.data.repository.SessionRepository
import com.capstone.nutrieasy.util.Result
import com.capstone.nutrieasy.util.uriToFile
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import okhttp3.RequestBody.Companion.toRequestBody
import javax.inject.Inject

@HiltViewModel
class ResultFragmentViewModel @Inject constructor(
    private val serviceRepository: ServiceRepository,
    private val authRepository: AuthRepository
): ViewModel() {
    private val _viewState = MutableLiveData(
        ResultFragmentViewState(
            isLoading = false,
            isTrackLoading = false,
            isDailyNutritionLoading = false,
            isDailyNutritionSuccess = false,
            isSuccess = false,
            isTrackSuccess = false,
            isError = false,
            isTrackError = false,
            isDailyNutritionError = false,
            errorMessage = "",
            data = null,
            dailyNutritionData = null,
            size = 1
        )
    )
    val viewState: LiveData<ResultFragmentViewState> = _viewState
    val nutritions_name = listOf("Calorie", "Sugar", "Fiber", "Protein")

    fun scan(uid: String, imageUri: Uri, context: Context){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isLoading = true,
                isDailyNutritionLoading = false,
                isError = false,
                isSuccess = false
            )

            val imageFile = uriToFile(imageUri, context)
            val requestUid = uid.toRequestBody("text/plain".toMediaType())
            val requestImageFile = imageFile.asRequestBody("image/jpg".toMediaType())
            val image = MultipartBody.Part.createFormData(
                "img",
                imageFile.name,
                requestImageFile
            )
            when(val result = serviceRepository.scan(requestUid, image)){
                is Result.Error -> {
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false,
                        isError = true,
                        errorMessage = result.message
                    )
                }
                is Result.Success -> {
                    _viewState.value = _viewState.value?.copy(
                        isDailyNutritionLoading = true,
                        isDailyNutritionSuccess = false,
                        isDailyNutritionError = false
                    )
                    val user = authRepository.getFirebaseUser()

                    when(val dailyNutritionResult = serviceRepository.getDailyNutrition(user!!.uid)){
                        is Result.Error -> {
                            _viewState.value = _viewState.value?.copy(
                                isDailyNutritionError = true,
                                errorMessage = dailyNutritionResult.message
                            )
                        }
                        is Result.Success -> {
                            val nutrition = dailyNutritionResult.data.filter { item ->
                                var isExist = false
                                for(name in nutritions_name){
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
                    _viewState.value = _viewState.value?.copy(
                        isLoading = false,
                        isSuccess = true,
                        data = result.data
                    )
                }
            }
        }
    }

    fun changeSize(newSize: Int){
        _viewState.value = _viewState.value?.copy(
            size = newSize
        )
    }

    fun track(){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isTrackLoading = true,
                isDailyNutritionLoading = true,
                isTrackSuccess = false,
                isTrackError = false
            )

            val data = _viewState.value!!.data!!
            val user = authRepository.getFirebaseUser()!!

            val result = serviceRepository.track(
                user.uid,
                data.foodName,
                data.imageUrl,
                _viewState.value!!.size
            )
            when(result){
                is Result.Error -> {
                    _viewState.value = _viewState.value?.copy(
                        isTrackLoading = false,
                        isTrackSuccess = false,
                        isTrackError = true,
                        errorMessage = result.message
                    )
                }
                is Result.Success -> {
                    _viewState.value = _viewState.value?.copy(
                        isDailyNutritionSuccess = false,
                        isDailyNutritionError = false
                    )

                    when(val dailyNutritionResult = serviceRepository.getDailyNutrition(user.uid)){
                        is Result.Error -> {
                            _viewState.value = _viewState.value?.copy(
                                isDailyNutritionLoading = false,
                                isDailyNutritionError = true,
                                errorMessage = dailyNutritionResult.message
                            )
                        }
                        is Result.Success -> {
                            val nutrition = dailyNutritionResult.data.filter { item ->
                                var isExist = false
                                for(name in nutritions_name){
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
                    _viewState.value = _viewState.value?.copy(
                        isTrackLoading = false,
                        isTrackSuccess = true,
                        isTrackError = false,
                    )
                }
            }
        }
    }

    fun getDailyNutrition(){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isDailyNutritionLoading = true,
                isDailyNutritionSuccess = false,
                isDailyNutritionError = false
            )

            val user = authRepository.getFirebaseUser()

            when(val result = serviceRepository.getDailyNutrition(user!!.uid)){
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
                        for(name in nutritions_name){
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
}