package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.paging.ExperimentalPagingApi
import com.example.storyapp.api.ApiService
import com.example.storyapp.dummy.DataDummy
import com.example.storyapp.dummy.FakeApiService
import com.example.storyapp.paging.StoryRepository
import com.example.storyapp.util.MainDispatcherRule
import com.example.storyapp.util.StandardDispatcherRule
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test

@ExperimentalPagingApi
@ExperimentalCoroutinesApi
class StoryRepositoryTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @get:Rule
    val standardDispatcher = StandardDispatcherRule()

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
        val actualStory = storyRepository.getStoriesRepo("bearer")
        actualStory.observeForTesting {
            Assert.assertNotNull(actualStory)
        }

    }

    @Test
    fun `when getStoriesLoc Should Not Null and return the same number of data`() = runTest {
        val expectedStory = DataDummy.generateDummyStoryResponse()
        val actualStory = storyRepository.getStoriesLocRepo("bearer")
        actualStory.observeForTesting {
            Assert.assertNotNull(actualStory)
            Result.success(actualStory).getOrDefault(actualStory).value!!.onSuccess { response ->
                Assert.assertEquals(expectedStory.listStory.size, response.size)
            }
        }
    }

    @Test
    fun `when postStory Should Not Null and not error`() = runTest {
        val expectedStory = DataDummy.generateDummyPostStoriesResponse()
        val dummyMultipart = DataDummy.generateDummyMultipartFile()
        val dummyRequestBody = DataDummy.generateDummyRequestBody()
        val actualStory = storyRepository.postStoryRepo("bearer", dummyMultipart,dummyRequestBody,dummyRequestBody,dummyRequestBody)
        actualStory.observeForTesting {
            Assert.assertNotNull(actualStory)
            Result.success(actualStory).getOrDefault(actualStory).value!!.onSuccess { response ->
                Assert.assertEquals(expectedStory.error, response.error)
            }
        }
    }

    @Test
    fun `when postLogin Should Not Null and not error`() = runTest {
        val expectedStory = DataDummy.generateDummyPostLoginResponse()
        val actualStory = storyRepository.postLoginRepo("email.com","password")
        actualStory.observeForTesting {
            Assert.assertNotNull(actualStory)
            Result.success(actualStory).getOrDefault(actualStory).value!!.onSuccess { response ->
                Assert.assertEquals(expectedStory.error, response.error)
                Assert.assertNotNull(response.loginResult)
            }
        }
    }

    @Test
    fun `when postRegister Should Not Null and not error`() = runTest {
        val expectedStory = DataDummy.generateDummyPostRegisterResponse()
        val actualStory = storyRepository.postRegisterRepo("nama","email.com","password")
        actualStory.observeForTesting {
            Assert.assertNotNull(actualStory)
            Result.success(actualStory).getOrDefault(actualStory).value!!.onSuccess { response ->
                Assert.assertEquals(expectedStory.error, response.error)
            }
        }
    }



}