package com.example.storyapp.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.example.storyapp.Story
import com.example.storyapp.api.ApiService
import com.example.storyapp.api.Stories
import com.example.storyapp.api.StoriesResponse

class QuotePagingSource(private val apiService: ApiService) : PagingSource<Int, Stories>() {
    private companion object {
        const val INITIAL_PAGE_INDEX = 1
    }
    override fun getRefreshKey(state: PagingState<Int, Stories>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }

    override suspend fun load(params: LoadParams<Int>): LoadResult<Int, Stories> {
        return try {
            val position = params.key ?: INITIAL_PAGE_INDEX
            val responseData = apiService.getStoriesPage("Bearer ",1,6)
            LoadResult.Page(
                data = responseData.listStory,
                prevKey = if (position == INITIAL_PAGE_INDEX) null else position - 1,
                nextKey = if (responseData.listStory.isNullOrEmpty()) null else position + 1
            )
        } catch (exception: Exception) {
            return LoadResult.Error(exception)
        }
    }

}