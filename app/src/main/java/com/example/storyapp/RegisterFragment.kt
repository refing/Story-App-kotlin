package com.example.storyapp

import android.content.ContentValues
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.*
import com.example.storyapp.api.ApiConfig
import com.example.storyapp.api.RegisterResponse
import com.example.storyapp.customview.EmailText
import com.example.storyapp.customview.LoginButton
import com.example.storyapp.customview.PasswordText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class RegisterFragment : Fragment(), View.OnClickListener {
    private lateinit var loginButton: LoginButton
    private lateinit var emailText: EmailText
    private lateinit var passwordText: PasswordText
    private lateinit var nameText: EditText
    private lateinit var progressBar: ProgressBar

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_register, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val btnlogin: Button = view.findViewById(R.id.btn_login)
        btnlogin.setOnClickListener(this)
        progressBar = view.findViewById(R.id.progressBar)
        loginButton = view.findViewById(R.id.login_button)
        emailText = view.findViewById(R.id.email_text)
        passwordText = view.findViewById(R.id.pw_text)
        nameText = view.findViewById(R.id.name_text)
        setMyButtonEnable()
        emailText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_EMAIL_ADDRESS
        passwordText.inputType = InputType.TYPE_CLASS_TEXT or InputType.TYPE_TEXT_VARIATION_PASSWORD
        emailText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        passwordText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {
            }
            override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
                setMyButtonEnable()
            }
            override fun afterTextChanged(s: Editable) {
            }
        })
        loginButton.setOnClickListener {
            postRegister(nameText.text.toString(),emailText.text.toString(),passwordText.text.toString())
            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(view.windowToken, 0)
        }
    }

    private fun setMyButtonEnable() {
        val result1 = emailText.text
        val result2 = passwordText.text
        loginButton.isEnabled = result1 != null && result1.toString().isNotEmpty() && result2 != null && result2.toString().isNotEmpty()
    }

    override fun onClick(v: View) {
        if (v.id == R.id.btn_login) {
            val mLoginFragment = LoginFragment()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, mLoginFragment, LoginFragment::class.java.simpleName)
                commit()
            }
        }
    }
    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun postRegister(name: String, email: String,password: String) {
        showLoading(true)
        val client = ApiConfig.getApiService().postRegister(name,email,password)
        client.enqueue(object : Callback<RegisterResponse> {
            override fun onResponse(
                call: Call<RegisterResponse>,
                response: Response<RegisterResponse>
            ) {
                showLoading(false)
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    Toast.makeText(activity, responseBody.message, Toast.LENGTH_LONG).show()
                }
                else {
                    Log.e(ContentValues.TAG, "onFailure: ${response.message()}")
                    Toast.makeText(activity, response.message(), Toast.LENGTH_LONG).show()
                }
            }
            override fun onFailure(call: Call<RegisterResponse>, t: Throwable) {
                showLoading(false)
                Log.e(ContentValues.TAG, "onFailure: ${t.message}")
            }
        })
    }
}