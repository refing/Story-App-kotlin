package com.example.storyapp.paging

import android.content.Context
import com.example.storyapp.api.ApiConfig

object Injection {
    fun provideRepository(context: Context): StoryRepository {
//        val database = StoryDatabase.getDatabase(context)
        val apiService = ApiConfig.getApiService()
//        return StoryRepository(database, apiService)
        return StoryRepository(apiService)
    }
}