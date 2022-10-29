package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.api.PostStoryResponse
import com.example.storyapp.api.Stories
import com.example.storyapp.dummy.DataDummy
import com.example.storyapp.paging.StoryRepository
import com.example.storyapp.util.MainDispatcherRule
import com.example.storyapp.util.StoryPagingSourceTest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.Mockito.`when`
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@ExperimentalPagingApi
@RunWith(MockitoJUnitRunner::class)
class MainViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val mainDispatcherRules = MainDispatcherRule()

    @Mock
    private lateinit var mockstoryRepository: StoryRepository

    private lateinit var mainViewModel: MainViewModel

    private val dummyToken = "token"

    @Before
    fun setUp() {
        mainViewModel = MainViewModel(mockstoryRepository)
    }


    @Test
    fun `when Get Stories Should Not Null and Return Success`() = runTest {
        val dummyStory = DataDummy.generateDummyStoriesEntity()
        val data: PagingData<Stories> = StoryPagingSourceTest.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<Stories>>()
        expectedStory.value = data
        `when`(mockstoryRepository.getStoriesRepo(dummyToken)).thenReturn(expectedStory)

        val actualStory: PagingData<Stories> = mainViewModel.getStoriesV(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DiffCallback,
            updateCallback = noopListUpdateCallback,
            workerDispatcher = Dispatchers.Main,
        )
        differ.submitData(actualStory)

        advanceUntilIdle()

        Mockito.verify(mockstoryRepository).getStoriesRepo(dummyToken)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)
    }

    @Test
    fun `when Get Stories with Location Should Not Null and Return Success`() = runTest {
        val dummyStory = DataDummy.generateDummyStoriesEntity()
        val expectedStory = MutableLiveData<Result<List<Stories>>>()
        expectedStory.value = Result.success(dummyStory)
        `when`(mockstoryRepository.getStoriesLocRepo(dummyToken)).thenReturn(expectedStory)

        val actualStory: Result<List<Stories>> = mainViewModel.getStoriesLocationV(dummyToken).getOrAwaitValue()

        Mockito.verify(mockstoryRepository).getStoriesLocRepo(dummyToken)
        Assert.assertNotNull(actualStory)
    }

    @Test
    fun `when Post Stories Should Return Success`() = runTest {
        val dummyStory = DataDummy.generateDummyPostStoriesResponse()
        val dummyMultipart = DataDummy.generateDummyMultipartFile()
        val dummyRequestBody = DataDummy.generateDummyRequestBody()
        val expectedStory = MutableLiveData<Result<PostStoryResponse>>()
        expectedStory.value = Result.success(dummyStory)
        Mockito.`when`(mockstoryRepository.postStoryRepo(dummyToken,
            dummyMultipart,dummyRequestBody,
            dummyRequestBody,dummyRequestBody)).thenReturn(expectedStory)

        val actualStory = mainViewModel.postStoryV(dummyToken,
            dummyMultipart,dummyRequestBody,
            dummyRequestBody,dummyRequestBody).getOrAwaitValue()

        Mockito.verify(mockstoryRepository).postStoryRepo(dummyToken,
            dummyMultipart,dummyRequestBody,
            dummyRequestBody,dummyRequestBody)
        Assert.assertNotNull(actualStory)
    }

    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}

