package com.example.storyapp.api

import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.*

interface ApiService {
    //post login
    @FormUrlEncoded
    @POST("login")
    suspend fun postLogin(
        @Field("email") email: String,
        @Field("password") password: String
    ): LoginResponse

    //post register
    @FormUrlEncoded
    @POST("register")
    suspend fun postRegister(
        @Field("name") name: String,
        @Field("email") email: String,
        @Field("password") password: String
    ): RegisterResponse

    //post stories need auth
    @Multipart
    @POST("stories")
    suspend fun postStory(
        @Header("Authorization") bearer: String?,
        @Part file: MultipartBody.Part,
        @Part("description") description: RequestBody,
        @Part("lat") lat: RequestBody,
        @Part("lon") lon: RequestBody,

    ): PostStoryResponse

    //get stories location need auth
    @GET("stories?location=1")
    suspend fun getStoriesLoc(
        @Header("Authorization") bearer: String?,

    ): StoriesResponse

    //get stories need auth page and size
    @GET("stories")
    suspend fun getStoriesPage(
        @Header("Authorization") bearer: String?,
        @Query("page") page: Int,
        @Query("size") size: Int

    ): StoriesResponse

}