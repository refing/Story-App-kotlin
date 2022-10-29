package com.example.storyapp

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.widget.Toast
import com.example.storyapp.customview.EmailText
import com.example.storyapp.customview.LoginButton
import com.example.storyapp.customview.PasswordText

class LoginActivity : AppCompatActivity() {
    private lateinit var loginButton: LoginButton
    private lateinit var emailText: EmailText
    private lateinit var passwordText: PasswordText

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