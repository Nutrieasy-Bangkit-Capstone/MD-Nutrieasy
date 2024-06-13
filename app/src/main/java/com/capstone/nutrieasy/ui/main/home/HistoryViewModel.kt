package com.capstone.nutrieasy.ui.main.home

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrieasy.data.repository.HomeRepository
import com.capstone.nutrieasy.data.repository.ProfileRepository
import com.capstone.nutrieasy.data.response.HistoryDetailResponse
import com.capstone.nutrieasy.data.response.HistoryResponse
import com.capstone.nutrieasy.helper.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(
    private val homeRepositoru: HomeRepository,

) : ViewModel() {

    fun getListFruit(): LiveData<PagingData<HistoryResponse>> = homeRepositoru.getListFruit().cachedIn(viewModelScope)

    fun getDetailHistory(id: String): LiveData<Result<HistoryDetailResponse>> = homeRepositoru.getDetailFruit(id)

}