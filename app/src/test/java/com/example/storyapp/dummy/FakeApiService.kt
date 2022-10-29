package com.example.storyapp.dummy

import com.example.storyapp.api.*
import okhttp3.MultipartBody
import okhttp3.RequestBody

class FakeApiService : ApiService {
    private val dummyStoriesResponse = DataDummy.generateDummyStoryResponse()
    private val dummyLoginResponse = DataDummy.generateDummyPostLoginResponse()
    private val dummyRegisterResponse = DataDummy.generateDummyPostRegisterResponse()
    private val dummyPostStoriesResponse = DataDummy.generateDummyPostStoriesResponse()

    override suspend fun postLogin(email: String, password: String): LoginResponse {
        return dummyLoginResponse
    }

    override suspend fun postRegister(
        name: String,
        email: String,
        password: String
    ): RegisterResponse {
        return dummyRegisterResponse
    }

    override suspend fun postStory(
        bearer: String?,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody
    ): PostStoryResponse {
        return dummyPostStoriesResponse
    }

    override suspend fun getStoriesLoc(bearer: String?): StoriesResponse {
        return dummyStoriesResponse
    }

    override suspend fun getStoriesPage(bearer: String?, page: Int, size: Int): StoriesResponse {
        return dummyStoriesResponse
    }

}