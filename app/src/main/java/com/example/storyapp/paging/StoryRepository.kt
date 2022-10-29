package com.example.storyapp.paging

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.liveData
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.example.storyapp.MapsActivity
import com.example.storyapp.Story
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.ApiService
import com.example.storyapp.api.Stories
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow
import com.example.storyapp.api.StoriesResponse
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class StoryRepository(private val apiService: ApiService) {
//    fun getStory(): LiveData<PagingData<Stories>> {
//        return Pager(
//            config = PagingConfig(
//                pageSize = 5
//            ),
//            pagingSourceFactory = {
//                StoryPagingSource(apiService,token)
//            }
//        ).liveData
//    }
    fun getStory(token:String): LiveData<PagingData<Stories>> {
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
//fun getHeadlineNews(): LiveData<Result<List<NewsEntity>>> = liveData {
//    emit(Result.Loading)
//    try {
//        val response = apiService.getNews(BuildConfig.API_KEY)
//        val articles = response.articles
//        val newsList = articles.map { article ->
//            NewsEntity(
//                article.title,
//                article.publishedAt,
//                article.urlToImage,
//                article.url
//            )
//        }
//        emit(Result.Success(newsList))
//    } catch (e: Exception) {
//        Log.d("NewsRepository", "getHeadlineNews: ${e.message.toString()} ")
//        emit(Result.Error(e.message.toString()))
//    }
//}

}