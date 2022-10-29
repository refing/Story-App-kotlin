package com.example.storyapp.dummy

import com.example.storyapp.api.*
import okhttp3.MultipartBody
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

object DataDummy {
    fun generateDummyStoriesEntity(): List<Stories> {
        val storiesList : MutableList<Stories> = arrayListOf()
        for (i in 0..10) {
            val story = Stories(
                "1",
                "ahmad",
                "halo",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                0.0,
                0.0,
            )
            storiesList.add(story)
        }
        return storiesList
    }
    fun generateDummyStoryResponse(): StoriesResponse {
        val storyList = ArrayList<Stories>()
        for (i in 0..10) {
            val story = Stories(
                "2",
                "ahmad",
                "hi",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                0.0,
                0.0,
            )
            storyList.add(story)
        }
        return StoriesResponse(false,"Data fetched succesfully", storyList)
    }
    fun generateDummyPostStoriesResponse(): PostStoryResponse {
        return PostStoryResponse(false,"success")
    }
    fun generateDummyPostLoginResponse(): LoginResponse {
        return LoginResponse(Login("1","abdul","token"),false,"success")
    }
    fun generateDummyPostRegisterResponse(): RegisterResponse {
        return RegisterResponse(false,"success")
    }
    fun generateDummyMultipartFile(): MultipartBody.Part {
        val dummyText = "text"
        return MultipartBody.Part.create(dummyText.toRequestBody())
    }

    fun generateDummyRequestBody(): RequestBody {
        val dummyText = "text"
        return dummyText.toRequestBody()
    }


}