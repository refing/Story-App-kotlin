package com.example.storyapp.paging

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.api.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class StoryRepository(private val apiService: ApiService) {
    fun getStoriesRepo(token:String): LiveData<PagingData<Stories>> {
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            pagingSourceFactory = {
                StoryPagingSource(apiService,token)
            }
        ).liveData
    }

    fun getStoriesLocRepo(token:String): LiveData<Result<List<Stories>>> = liveData{
        try {
            val response = apiService.getStoriesLoc("Bearer $token")
            val liststory = response.listStory
            emit(Result.success(liststory))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    fun postLoginRepo(email:String,
                      password:String):LiveData<Result<LoginResponse>> = liveData{
        try {
            val response = apiService.postLogin(email,password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    fun postRegisterRepo(name:String,
                         email:String,
                         password:String):LiveData<Result<RegisterResponse>> = liveData{
        try {
            val response = apiService.postRegister(name,email,password)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }

    fun postStoryRepo(token:String,
                      imageMultipart: MultipartBody.Part,
                      description:RequestBody,
                      lat2:RequestBody,
                      lon2:RequestBody): LiveData<Result<PostStoryResponse>> = liveData{
        try {
            val response = apiService.postStory("Bearer $token",imageMultipart, description, lat2, lon2)
            emit(Result.success(response))
        } catch (e: Exception) {
            e.printStackTrace()
            emit(Result.failure(e))
        }
    }
    companion object {
        @Volatile
        private var instance: StoryRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): StoryRepository =
            instance ?: synchronized(this) {
                instance ?: StoryRepository(apiService)
            }.also { instance = it }
    }
}