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

    @Mock
    private lateinit var apiService: ApiService

    @Mock
    private lateinit var mockstoryRepository: StoryRepository

    private lateinit var storyRepository: StoryRepository
    private val dummyToken = "token"

    @Before
    fun setUp() {
        storyRepository = StoryRepository(apiService)
    }

    @Test
    fun `Get stories with pager - successfully`() = runTest {
        val dummyStories = DataDummy.generateDummyStoriesEntity()
        val data = StoryPagingSourceTest.snapshot(dummyStories)

        val expectedResult = flowOf(data)

        Mockito.`when`(mockstoryRepository.getStory(dummyToken)).thenReturn(expectedResult)

        mockstoryRepository.getStory(dummyToken).observe(this) { actualResult ->
            val differ = AsyncPagingDataDiffer(
                diffCallback = StoryListAdapter.DiffCallback,
                updateCallback = noopListUpdateCallback,
                mainDispatcher = coroutinesTestRule.testDispatcher,
                workerDispatcher = coroutinesTestRule.testDispatcher
            )
            differ.submitData(actualResult)

            Assert.assertNotNull(differ.snapshot())
            Assert.assertEquals(
                dummyStoriesResponse.storyResponseItems.size,
                differ.snapshot().size
            )
        }

    }

}