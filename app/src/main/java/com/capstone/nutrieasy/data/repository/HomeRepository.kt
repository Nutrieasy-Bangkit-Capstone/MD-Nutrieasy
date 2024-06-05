package com.capstone.nutrieasy.data.repository

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import dagger.hilt.android.qualifiers.ApplicationContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import javax.inject.Inject
import com.capstone.nutrieasy.helper.Result
import com.capstone.nutrieasy.helper.convertErrorData

class HomeRepository @Inject constructor(
    private val apiService: ApiService,
    @ApplicationContext private val context: Context
) {
    private val sharedPreferences = context.getSharedPreferences(context.packageName, Context.MODE_PRIVATE)
    private val authorization = "Bearer ${
        sharedPreferences.getString("PREF_TOKEN", "")
    }"

    fun getListFruit(): LiveData<PagingData<FruitResponse>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                FruitSource(apiService, authorization)
            }
        ).liveData
    }

    fun getDetailFruit(id: String): LiveData<Result<DetailFruitResponse>> {
        val data: MutableLiveData<Result<DetailFruitResponse>> = MutableLiveData()

        try {
            apiService.getDetailStory(authorization, id)
                .enqueue(object : Callback<DetailFruitResponse> {
                    override fun onResponse(
                        call: Call<DetailFruitResponse>,
                        response: Response<DetailFruitResponse>
                    ) {
                        if (response.isSuccessful) {
                            data.postValue(Result.Success(response.body() as DetailFruitResponse))
                        } else {
                            val errorData = response.errorBody()?.string()?.let { convertErrorData(it) }
                            data.postValue(
                                Result.Error(
                                    errorData?.message ?: "error get data",
                                    response.code(),
                                    null
                                )
                            )
                        }
                    }

                    override fun onFailure(call: Call<DetailFruitResponse>, t: Throwable) {
                        data.postValue(Result.Error(t.message.toString(), null, null))
                    }

                })
        } catch (e: Exception) {
            e.printStackTrace()
            data.postValue(Result.Error("error convert data", null, null))
        }


        return data
    }

}