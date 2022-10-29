package com.example.storyapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Story(
    var id: String = "0",
    var name: String? = null,
    var description: String? = null,
    var photoUrl: String? = null,
    var lat: Double? = null,
    var lon: Double? = null,
) : Parcelable