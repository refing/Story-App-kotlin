package com.example.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val mFragmentManager = supportFragmentManager
        val mLoginFragment = LoginFragment()
        val fragment = mFragmentManager.findFragmentByTag(LoginFragment::class.java.simpleName)
        if (fragment !is LoginFragment) {
            mFragmentManager
                .beginTransaction()
                .add(R.id.frame_container, mLoginFragment, LoginFragment::class.java.simpleName)
                .commit()
        }
    }
}