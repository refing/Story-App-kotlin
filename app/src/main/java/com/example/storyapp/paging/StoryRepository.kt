package com.example.storyapp.paging

import androidx.lifecycle.LiveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.api.ApiService
import com.example.storyapp.api.Stories
import com.example.storyapp.api.StoriesResponse
import retrofit2.Call

class StoryRepository(private val apiService: ApiService) {
    fun getStory(): LiveData<PagingData<Stories>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                QuotePagingSource(apiService)
            }
        ).liveData
    }
}