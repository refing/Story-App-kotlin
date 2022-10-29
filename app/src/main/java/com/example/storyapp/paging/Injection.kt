package com.example.storyapp.paging

import android.content.Context
import com.example.storyapp.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
        val apiService = ApiConfig.getApiService()
        val token = context.getSharedPreferences("session_pref", Context.MODE_PRIVATE).getString("token","").toString()
        return StoryRepository(apiService)
    }
}