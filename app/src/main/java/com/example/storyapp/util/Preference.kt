package com.example.storyapp.util

import android.content.Context

internal class Preference(context: Context) {
    companion object {
        private const val PREFS_NAME = "session_pref"
        private const val NAME = "name"
        private const val TOKEN = "token"
    }
    private val preferences = context.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE)
    fun setSession(value: SessionModel) {
        val editor = preferences.edit()
        editor.putString(NAME, value.name)
        editor.putString(TOKEN, value.token)
        editor.apply()
    }
    fun getSession(): SessionModel {
        val model = SessionModel()
        model.name = preferences.getString(NAME, "")
        model.token = preferences.getString(TOKEN, "")
        return model
    }
}