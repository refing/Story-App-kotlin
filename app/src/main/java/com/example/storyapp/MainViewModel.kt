package com.example.storyapp

import android.content.Context
import androidx.lifecycle.*
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.example.storyapp.api.Stories
import com.example.storyapp.paging.Injection
import com.example.storyapp.paging.StoryRepository


class MainViewModel (private val storyRepository: StoryRepository): ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun getStories(token: String): LiveData<PagingData<Stories>> =
        storyRepository.getStory(token).cachedIn(viewModelScope)

    fun getStoriesLocation(token: String) =
        storyRepository.getStoriesLocRepo(token)

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