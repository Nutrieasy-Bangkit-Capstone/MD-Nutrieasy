package com.capstone.nutrieasy.di

import android.content.Context
import com.capstone.nutrieasy.data.api.ApiConfig
import com.capstone.nutrieasy.data.local.pref.TokenPreference
import com.capstone.nutrieasy.data.local.pref.dataStore
import com.capstone.nutrieasy.data.repository.ProfileRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.runBlocking

@Module
@InstallIn(ViewModelComponent::class)
object ProfileRepositoryModule {
    @Provides
    fun providesProfileRepository(@ApplicationContext context: Context): ProfileRepository{
        val pref = TokenPreference.getInstance(context.dataStore)
        val token = runBlocking {
            pref.getSession().first()
        }
        val appService = ApiConfig.getAppService(token)
        return ProfileRepository(appService)
    }
}