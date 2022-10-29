package com.example.storyapp.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.*

interface ApiService {
    //post login
    @FormUrlEncoded
    @POST("login")
    fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<LoginResponse>

    //post register
    @FormUrlEncoded
    @POST("register")
    fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): Call<RegisterResponse>

    //get stories need auth
    @GET("stories")
    fun getStories(
        @Header("Authorization") bearer: String?,

    ): Call<StoriesResponse>

    //post stories need auth
    @Multipart
    @POST("stories")
    fun postStory(
        @Header("Authorization") bearer: String?,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,

    ): Call<PostStoryResponse>

    //get stories location need auth
    @GET("stories?location=1")
    fun getStoriesLoc(
        @Header("Authorization") bearer: String?,

        ): Call<StoriesResponse>


}