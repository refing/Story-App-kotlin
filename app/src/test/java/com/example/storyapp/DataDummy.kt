package com.example.storyapp

import com.example.storyapp.api.Stories
import com.example.storyapp.api.StoriesResponse

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
}