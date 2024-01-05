package com.kasandi.storyapp.di

import android.content.Context
import com.kasandi.storyapp.data.StoryRepository
import com.kasandi.storyapp.data.UserRepository
import com.kasandi.storyapp.data.local.preference.UserPreference
import com.kasandi.storyapp.data.local.preference.dataStore
import com.kasandi.storyapp.data.remote.retrofit.ApiConfig

object Injection {

    fun provideUserRepository(context: Context): UserRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return UserRepository(pref, apiService)
    }

    fun provideStoryRepository(context: Context): StoryRepository {
        val pref = UserPreference.getInstance(context.dataStore)
        val apiService = ApiConfig.getApiService()
        return StoryRepository(pref, apiService)
    }
}