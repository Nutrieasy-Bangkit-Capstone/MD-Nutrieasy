package com.capstone.nutrieasy.ui.main.home

import androidx.lifecycle.LiveData
import androidx.paging.PagingData
import androidx.paging.cachedIn
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.capstone.nutrieasy.data.repository.HomeRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class HistoryViewModel @Inject constructor(private val repository: HomeRepository) : ViewModel() {

    fun getListFruit(): LiveData<PagingData<FruitResponse>> = repository.getListFruit().cachedIn(viewModelScope)

    fun getDetailFruit(id: String): LiveData<Result<DetailFruitResponse>> = repository.getDetailFruit(id)

}