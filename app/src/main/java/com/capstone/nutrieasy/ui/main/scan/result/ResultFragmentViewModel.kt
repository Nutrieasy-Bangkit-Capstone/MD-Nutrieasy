package com.capstone.nutrieasy.ui.main.scan.result

import android.content.Context
import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrieasy.data.repository.ServiceRepository
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
    private val serviceRepository: ServiceRepository
): ViewModel() {
    private val _viewState = MutableLiveData(
        ResultFragmentViewState(
            isLoading = false,
            isSuccess = false,
            isError = false,
            errorMessage = "",
            data = null
        )
    )
    val viewState: LiveData<ResultFragmentViewState> = _viewState

    fun scan(uid: String, imageUri: Uri, context: Context){
        viewModelScope.launch {
            _viewState.value = _viewState.value?.copy(
                isLoading = true,
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
                        isLoading = false,
                        isSuccess = true,
                        data = result.data
                    )
                }
            }
        }
    }
}