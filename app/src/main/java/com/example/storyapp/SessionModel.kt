package com.example.storyapp

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SessionModel (
    var name: String? = null,
    var token: String? = null,
) : Parcelable