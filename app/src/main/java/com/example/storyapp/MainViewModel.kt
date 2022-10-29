package com.example.storyapp

import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.api.Stories
import com.example.storyapp.paging.StoryRepository
import okhttp3.MultipartBody
import okhttp3.RequestBody


class MainViewModel (private val storyRepository: StoryRepository): ViewModel() {
    fun getStoriesV(token: String): LiveData<PagingData<Stories>> =
        storyRepository.getStoriesRepo(token).cachedIn(viewModelScope)

    fun getStoriesLocationV(token: String) =
        storyRepository.getStoriesLocRepo(token)

    fun postStoryV(token:String,
                  imageMultipart: MultipartBody.Part,
                  description: RequestBody,
                  lat2: RequestBody,
                  lon2: RequestBody) =
        storyRepository.postStoryRepo(token,imageMultipart,description,lat2,lon2)
}
