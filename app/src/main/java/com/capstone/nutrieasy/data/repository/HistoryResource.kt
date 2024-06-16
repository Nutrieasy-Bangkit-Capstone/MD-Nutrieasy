package com.capstone.nutrieasy.data.repository

import android.util.Log
import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.capstone.nutrieasy.data.api.AppService
import com.capstone.nutrieasy.data.response.HistoryResponse

class HistoryResource(private val authRepository: AuthRepository, private val apiService: AppService): PagingSource<Int, HistoryResponse>() {


    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }

    override fun getRefreshKey(state: PagingState<Int, HistoryResponse>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, HistoryResponse> {
        return try {
            val page = params.key ?: INITIAL_PAGE_INDEX
            val user = authRepository.getFirebaseUser()
            val uid = user?.uid!!
            val responseData = apiService.getUserHistory(uid)

            val prefKey = if (page == INITIAL_PAGE_INDEX) null else page - 1
            val nextKey = if (responseData.listFruit.isNullOrEmpty()) null else page + 1

            val data = responseData.listFruit ?: emptyList()

            LoadResult.Page(
                data = data,
                prevKey = prefKey,
                nextKey = nextKey
            )
        }catch (e: Exception) {
            e.printStackTrace()
            Log.e(HistoryResource::class.java.simpleName, "Error get data ${e.message}")
            LoadResult.Error(e)
        }
    }

}