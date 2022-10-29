package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.paging.*
import androidx.recyclerview.widget.ListUpdateCallback
import com.example.storyapp.api.Stories
import com.example.storyapp.paging.StoryRepository
//import com.example.storyapp.paging.Result
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
    private lateinit var storyRepository: StoryRepository

    @Mock
    private lateinit var mainViewModel: MainViewModel

    private val dummyToken = "token"

    @Test
    fun `when Get Stories Should Not Null and Return Success`() = runTest {
        val dummyStory = DataDummy.generateDummyStoriesEntity()
        val data: PagingData<Stories> = StoryPagingSourceTest.snapshot(dummyStory)
        val expectedStory = MutableLiveData<PagingData<Stories>>()
        expectedStory.value = data
        `when`(mainViewModel.getStories(dummyToken)).thenReturn(expectedStory)

        val actualStory: PagingData<Stories> = mainViewModel.getStories(dummyToken).getOrAwaitValue()

        val differ = AsyncPagingDataDiffer(
            diffCallback = StoryAdapter.DiffCallback,
            updateCallback = noopListUpdateCallback,
            mainDispatcher = mainDispatcherRules.testDispatcher,
            workerDispatcher = mainDispatcherRules.testDispatcher
        )
        differ.submitData(actualStory)

        advanceUntilIdle()

        Mockito.verify(mainViewModel).getStories(dummyToken)
        Assert.assertNotNull(differ.snapshot())
        Assert.assertEquals(dummyStory.size, differ.snapshot().size)

    }

    @Test
    fun `when Get Stories with Location Should Not Null and Return Success`() = runTest {
        val dummyStory = DataDummy.generateDummyStoriesEntity()
//        val data: PagingData<Stories> = StoryPagingSourceTest.snapshot(dummyStory)
        val expectedStory = MutableLiveData<Result<List<Stories>>>()
//        LiveData<Result<List<Stories>>>
        expectedStory.value = Result.success(dummyStory)
        `when`(mainViewModel.getStoriesLocation(dummyToken)).thenReturn(expectedStory)

        val actualStory = mainViewModel.getStoriesLocation(dummyToken).getOrAwaitValue()

        actualStory.value.let {
            assertThat(it, `is`(notNullValue()))
            if (it is Resource.Success) {
                it.data?.let { data ->
                    assertTrue(data.films.isNotEmpty())
                    assertTrue(data.species.isNotEmpty())
                }
            }
        }
        Mockito.verify(mainViewModel).getStories(dummyToken)
        Assert.assertNotNull(actualStory)
        Assert.assertTrue(actualStory is Result.success(dummyStory))
//        Assert.assertEquals(dummyStory.size, (actualStory as Result.Success).data.size)

    }

    val noopListUpdateCallback = object : ListUpdateCallback {
        override fun onInserted(position: Int, count: Int) {}
        override fun onRemoved(position: Int, count: Int) {}
        override fun onMoved(fromPosition: Int, toPosition: Int) {}
        override fun onChanged(position: Int, count: Int, payload: Any?) {}
    }
}

