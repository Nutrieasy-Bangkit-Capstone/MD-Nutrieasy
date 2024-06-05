//package com.capstone.nutrieasy.data.repository
//
//import android.util.Log
//import androidx.paging.PagingSource
//import androidx.paging.PagingState
//
//class FruitSource(private val apiService: ApiService, private val authorization: String): PagingSource<Int, FruitResponse>() {
//
//    private companion object {
//        const val INITIAL_PAGE_INDEX = 1
//    }
//
//    override fun getRefreshKey(state: PagingState<Int, FruitResponse>): Int? {
//        return state.anchorPosition?.let { anchorPosition ->
//            val anchorPage = state.closestPageToPosition(anchorPosition)
//            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
//        }
//    }
//
//    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, FruitResponse> {
//        return try {
//            val page = params.key ?: INITIAL_PAGE_INDEX
//            val responseData = apiService.getListFruit(authorization, page, params.loadSize)
//
//            val prefKey = if (page == INITIAL_PAGE_INDEX) null else page - 1
//            val nextKey = if (responseData.listStory.isNullOrEmpty()) null else page + 1
//
//            LoadResult.Page(
//                data = responseData.listFruit as List,
//                prevKey = prefKey,
//                nextKey = nextKey
//            )
//        }catch (e: Exception) {
//            e.printStackTrace()
//            Log.e(FruitSource::class.java.simpleName, "Error get data ${e.message}")
//            LoadResult.Error(e)
//        }
//    }
//
//}