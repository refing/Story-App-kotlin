package com.example.storyapp

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.text.Editable
import android.text.InputType
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.ProgressBar
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.storyapp.api.Login
import com.example.storyapp.customview.EmailText
import com.example.storyapp.customview.LoginButton
import com.example.storyapp.customview.PasswordText
import com.example.storyapp.util.Preference
import com.example.storyapp.util.SessionModel


class LoginFragment : Fragment(), View.OnClickListener  {

    private lateinit var sessionModel: SessionModel

    private lateinit var loginButton: LoginButton
    private lateinit var emailText: EmailText
    private lateinit var passwordText: PasswordText
    private lateinit var progressBar: ProgressBar
    private lateinit var btnReg: Button
    private lateinit var userPreference : Preference



    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.fragment_login, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val factory: ViewModelFactory = ViewModelFactory.getInstance(requireActivity())
        val mainViewModel: LoginViewModel by viewModels {
            factory
        }

        btnReg = view.findViewById(R.id.btn_register)
        btnReg.setOnClickListener(this)
        progressBar = view.findViewById(R.id.progressBar)
        loginButton = view.findViewById(R.id.login_button)
        emailText = view.findViewById(R.id.email_text)
        passwordText = view.findViewById(R.id.pw_text)

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
            showLoading(true)
            mainViewModel.postLoginV(emailText.text.toString(),passwordText.text.toString()).observe(viewLifecycleOwner) { result ->
                result.onSuccess {  response ->
                    Toast.makeText(activity, "Login berhasil.", Toast.LENGTH_SHORT).show()
                    setSessionData(response.loginResult)
                    val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                    imm.hideSoftInputFromWindow(view.windowToken, 0)
                }
                result.onFailure {
                    Log.e("login fragment on failure", result.toString())
                    Toast.makeText(activity, "Login gagal.", Toast.LENGTH_SHORT).show()
                    showLoading(false)
                }
            }

        }
    }
    override fun onClick(v: View) {
        if (v.id == R.id.btn_register) {
            val mRegisterFragment = RegisterFragment()
            val mFragmentManager = parentFragmentManager
            mFragmentManager.beginTransaction().apply {
                replace(R.id.frame_container, mRegisterFragment, RegisterFragment::class.java.simpleName)
                commit()
            }
        }
    }
    private fun setMyButtonEnable() {
        val result1 = emailText.text
        val result2 = passwordText.text
        loginButton.isEnabled = result1 != null && result1.toString().isNotEmpty() && result2 != null && result2.toString().isNotEmpty()
    }
    private fun showLoading(isLoading: Boolean) {
        progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
    }
    private fun setSessionData(login: Login) {
        userPreference = Preference(this.requireContext())
        sessionModel = SessionModel(login.name,login.token)
        userPreference.setSession(sessionModel)
        val mRegisterFragment = RegisterFragment()
        val mFragmentManager = parentFragmentManager
        mFragmentManager.beginTransaction().apply {
            replace(R.id.frame_container, mRegisterFragment, RegisterFragment::class.java.simpleName)
            commit()
        }

        val moveWithObjectIntent = Intent(activity, MainActivity::class.java)
        moveWithObjectIntent.putExtra(MainActivity.EXTRA_NAME, login.name)
        moveWithObjectIntent.putExtra(MainActivity.EXTRA_TOKEN, login.token)
        moveWithObjectIntent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
        startActivity(moveWithObjectIntent)
        activity?.finish()
    }

    override fun onStart() {
        super.onStart()
        userPreference = Preference(this.requireContext())
        if (userPreference.getSession().name.toString() != "" && userPreference.getSession().token.toString() != "") {
            val i = Intent(activity, MainActivity::class.java)
            i.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
            startActivity(i)
            activity?.finish()
        }
    }
}