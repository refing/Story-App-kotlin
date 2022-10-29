package com.example.storyapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
class Story(
    val id: String,
    val name: String,
    val description: String,
    val photoUrl: String,
    val lat: Double? = null,
    val lon: Double? = null,
) : Parcelable