package com.example.storyapp

import com.example.storyapp.api.Stories

object DataDummy {
    fun generateDummyStoriesEntity(): List<Stories> {
        val storiesList : MutableList<Stories> = arrayListOf()
        for (i in 0..10) {
            val story = Stories(
                "Title $i",
                "2022-02-22T22:22:22Z",
                "https://dicoding-web-img.sgp1.cdn.digitaloceanspaces.com/original/commons/feature-1-kurikulum-global-3.png",
                "https://www.dicoding.com/",
                0.0,
                0.0,
            )
            storiesList.add(story)
        }
        return storiesList
    }
}