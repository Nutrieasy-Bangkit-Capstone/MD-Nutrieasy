package com.capstone.nutrieasy.di

import android.content.Context
import com.capstone.nutrieasy.data.local.pref.TokenPreference
import com.capstone.nutrieasy.data.local.pref.dataStore
import com.capstone.nutrieasy.data.repository.SessionRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.components.ViewModelComponent
import dagger.hilt.android.qualifiers.ApplicationContext

@Module
@InstallIn(ViewModelComponent::class)
object SessionRepositoryModule {
    @Provides
    fun provideSessionRepository(@ApplicationContext context: Context): SessionRepository{
        return SessionRepository(
            TokenPreference.getInstance(context.dataStore)
        )
    }
}