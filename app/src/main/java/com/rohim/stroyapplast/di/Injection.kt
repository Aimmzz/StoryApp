package com.rohim.stroyapplast.di

import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.datastore.preferences.preferencesDataStore
import com.rohim.stroyapplast.data.preference.UserPreference
import com.rohim.stroyapplast.data.service.ApiConfig
import com.rohim.stroyapplast.repositori.UserRepository

val Context.dataStore: DataStore<Preferences> by preferencesDataStore("storyapplast")
object Injection {
    fun provideRepository(context: Context): UserRepository {
        val preferences = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository.getInstance(preferences, apiService)
    }
//    fun provideRepo(context: Context): StoryRepository {
//        val pref = Preference2(context)
//        val token= pref.getToken().toString()
//        val database = StoryDatabase.getDatabase(context)
//        val apiService = ApiConfig.getApiService()
//        return StoryRepository(database, apiService, token)
//    }
}