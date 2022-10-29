package com.example.storyapp

import com.example.storyapp.api.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call

class FakeApiService : ApiService {
    private val dummyResponse = DataDummy.generateDummyStoryResponse()
    override fun postLogin(email: String, password: String): Call<LoginResponse> {
        TODO("Not yet implemented")
    }

    override fun postRegister(
        name: String,
        email: String,
        password: String
    ): Call<RegisterResponse> {
        TODO("Not yet implemented")
    }

    override fun postStory(
        bearer: String?,
        file: MultipartBody.Part,
        description: RequestBody,
        lat: RequestBody,
        lon: RequestBody
    ): Call<PostStoryResponse> {
        TODO("Not yet implemented")
    }

    override suspend fun getStoriesLoc(bearer: String?): StoriesResponse {
        return dummyResponse
    }

    override suspend fun getStoriesPage(bearer: String?, page: Int, size: Int): StoriesResponse {
        return dummyResponse
    }

}