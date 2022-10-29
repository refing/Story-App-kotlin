package com.example.storyapp

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.MutableLiveData
import com.example.storyapp.api.LoginResponse
import com.example.storyapp.api.RegisterResponse
import com.example.storyapp.dummy.DataDummy
import com.example.storyapp.paging.StoryRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.mockito.Mock
import org.mockito.Mockito
import org.mockito.junit.MockitoJUnitRunner

@ExperimentalCoroutinesApi
@RunWith(MockitoJUnitRunner::class)
class LoginViewModelTest {
    @get:Rule
    val instantExecutorRule = InstantTaskExecutorRule()

    @Mock
    private lateinit var mockstoryRepository: StoryRepository

    private lateinit var loginViewModel: LoginViewModel

    @Before
    fun setUp() {
        loginViewModel = LoginViewModel(mockstoryRepository)
    }

    @Test
    fun `when Post Login Should Return Success`() = runTest {
        val dummyStory = DataDummy.generateDummyPostLoginResponse()
        val expectedStory = MutableLiveData<Result<LoginResponse>>()
        expectedStory.value = Result.success(dummyStory)
        Mockito.`when`(mockstoryRepository.postLoginRepo("email","pass")).thenReturn(expectedStory)

        val actualStory = loginViewModel.postLoginV("email","pass").getOrAwaitValue()

        Mockito.verify(mockstoryRepository).postLoginRepo("email","pass")
        Assert.assertNotNull(actualStory)
    }

    @Test
    fun `when Post Register Should Return Success`() = runTest {
        val dummyStory = DataDummy.generateDummyPostRegisterResponse()
        val expectedStory = MutableLiveData<Result<RegisterResponse>>()
        expectedStory.value = Result.success(dummyStory)
        Mockito.`when`(mockstoryRepository.postRegisterRepo("name","email","pass")).thenReturn(expectedStory)

        val actualStory = loginViewModel.postRegisterV("name","email","pass").getOrAwaitValue()

        Mockito.verify(mockstoryRepository).postRegisterRepo("name","email","pass")
        Assert.assertNotNull(actualStory)
    }
}