package com.capstone.nutrieasy.data.local.pref

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.core.stringPreferencesKey
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore: DataStore<Preferences> by preferencesDataStore(name = "session")
class TokenPreference private constructor(private val dataStore: DataStore<Preferences>) {
    suspend fun saveToken(token: String){
        dataStore.edit { preferences ->
            preferences[TOKEN] = token
        }
    }

    fun getSession(): Flow<String> {
        return dataStore.data.map { preferences ->
            preferences[TOKEN] ?: ""
        }
    }

    suspend fun logout(){
        dataStore.edit { preferences ->
            preferences.clear()
        }
    }

    companion object{
        @Volatile
        private var INSTANCE: TokenPreference? = null

        private val TOKEN = stringPreferencesKey("token")

        fun getInstance(dataStore: DataStore<Preferences>): TokenPreference{
            return INSTANCE ?: synchronized(this){
                val instance = TokenPreference(dataStore)
                INSTANCE = instance
                instance
            }
        }
    }
}