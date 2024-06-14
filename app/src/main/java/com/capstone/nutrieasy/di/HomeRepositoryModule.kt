package com.capstone.nutrieasy.di

import android.content.Context
import com.capstone.nutrieasy.data.api.ApiConfig
import com.capstone.nutrieasy.data.local.pref.TokenPreference
import com.capstone.nutrieasy.data.local.pref.dataStore
import com.capstone.nutrieasy.data.repository.AuthRepository
import com.capstone.nutrieasy.data.repository.HomeRepository
import com.capstone.nutrieasy.data.repository.ProfileRepository
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Module
@InstallIn(ViewModelComponent::class)
object HomeRepositoryModule {
    @Provides
    fun provideHomeRepository(@ApplicationContext context: Context): HomeRepository {
        val pref = TokenPreference.getInstance(context.dataStore)
        val token = runBlocking {
            pref.getSession().first()
        }
        val apiService = ApiConfig.getAppService(token)
        return HomeRepository(apiService, context)
    }
}