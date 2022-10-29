package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.AsyncPagingDataDiffer
import com.example.storyapp.api.ApiService
import com.example.storyapp.paging.StoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.mockito.Mock
import org.mockito.Mockito

@ExperimentalCoroutinesApi
class StoryRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRule = MainDispatcherRule()

    private lateinit var apiService: ApiService
    private lateinit var storyRepository: StoryRepository

    @Before
    fun setUp() {
        apiService = FakeApiService()
        storyRepository = StoryRepository(apiService)
    }

    @Test
    fun `when getStoriesPage Should Not Null`() = runTest {
        val expectedNews = DataDummy.generateDummyStoryResponse()
        val actualNews = apiService.getStoriesPage("bearer",1,10)
        Assert.assertNotNull(actualNews)
        Assert.assertEquals(expectedNews.listStory.size, actualNews.listStory.size)
    }

    @Test
    fun `when getStoriesLoc Should Not Null`() = runTest {
        val expectedNews = DataDummy.generateDummyStoryResponse()
        val actualNews = apiService.getStoriesLoc("bearer")
        Assert.assertNotNull(actualNews)
        Assert.assertEquals(expectedNews.listStory.size, actualNews.listStory.size)
    }


}