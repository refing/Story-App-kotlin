package com.example.storyapp.util

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class SessionModel (
    var name: String? = null,
    var token: String? = null,
) : Parcelable