package com.example.storyapp

import android.content.Context
import android.util.Log
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.Stories
import com.example.storyapp.api.StoriesResponse
import com.example.storyapp.paging.Injection
import com.example.storyapp.paging.StoryRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class MainViewModel(storyRepository: StoryRepository): ViewModel() {
    val listStory: LiveData<PagingData<Stories>> =
        storyRepository.getStory().cachedIn(viewModelScope)

    private val _listStoryLoc = MutableLiveData<List<Stories>>()
    val listStoryLoc: LiveData<List<Stories>> = _listStoryLoc
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    companion object{
        private const val TAG = "MainViewModel"
    }

    fun getStoriesLoc(token: String) {
        _isLoading.value = true
        val client = ApiConfig.getApiService().getStoriesLoc("Bearer ${token}")
        client.enqueue(object : Callback<StoriesResponse> {
            override fun onResponse(
                call: Call<StoriesResponse>,
                response: Response<StoriesResponse>
            ) {
                _isLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _listStoryLoc.value = response.body()?.listStory
                } else {
                    Log.e(TAG, "onFailure: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<StoriesResponse>, t: Throwable) {
                _isLoading.value = false
                Log.e(TAG, "onFailure: ${t.message}")
            }
        })
    }
}
class ViewModelFactory(private val context: Context) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return MainViewModel(Injection.provideRepository(context)) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}