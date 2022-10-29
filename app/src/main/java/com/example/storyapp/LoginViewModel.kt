package com.example.storyapp

import androidx.lifecycle.ViewModel
import com.example.storyapp.paging.StoryRepository

class LoginViewModel (private val storyRepository: StoryRepository): ViewModel() {
    fun postLoginV(email:String,password:String) =
        storyRepository.postLoginRepo(email,password)

    fun postRegisterV(name:String,email:String,password:String) =
        storyRepository.postRegisterRepo(name,email,password)

}