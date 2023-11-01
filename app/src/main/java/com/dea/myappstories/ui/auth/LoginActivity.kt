package com.dea.myappstories.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dea.myappstories.ui.main.MainActivity
import com.dea.myappstories.R
import com.dea.myappstories.databinding.ActivityLoginBinding
import com.dea.myappstories.model.UserModel
import com.dea.myappstories.ui.ViewModelFactory
import com.dea.myappstories.utils.Result


class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var factory: ViewModelFactory
    private val loginViewModel: LoginViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupAction()
        setupViewModel()

        binding.regisButton.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val login = ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(100)
        val register = ObjectAnimator.ofFloat(binding.regisButton, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val message = ObjectAnimator.ofFloat(binding.messageTextView, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val textRegis = ObjectAnimator.ofFloat(binding.regisTextView, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(title, message, email, password, login, textRegis, register)
            start()
        }
    }

    private fun setupAction() {
        binding.apply {
            if (emailEditText.length() == 0 && passwordEditText.length() == 0) {
                emailEditText.error = getString(R.string.required_field_email)
                passwordEditText.setError(getString(R.string.required_field_password), null)
                loginButton.isEnabled = false
            } else if (emailEditText.length() != 0 && passwordEditText.length() != 0) {
                loginButton.isEnabled = true
            }

            emailEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    loginButton.isEnabled = emailEditText.text!!.isNotEmpty() && passwordEditText.text!!.isNotEmpty()
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            passwordEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    loginButton.isEnabled = passwordEditText.length() >= 8
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            loginButton.setOnClickListener {
                postText()
                loginViewModel.login()
            }
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun showToast() {
        loginViewModel.toastText.observe(this@LoginActivity) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this@LoginActivity, toastText, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun showLoading(b : Boolean) {
            binding.pbLogin.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun postText() {
        binding.apply {
            loginViewModel.doLogin(
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            ).observe(this@LoginActivity){result ->
                if (result != null){
                    when (result){
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            Log.d("LoginActivity", "token: ${result.data.loginResult?.token}")
                            AlertDialog.Builder(this@LoginActivity).apply {
                                setTitle(getString(R.string.alert_title_login))
                                setMessage(getString(R.string.alert_message_login))
                                setPositiveButton(R.string.alert_button_login) { _ , _ ->
                                    saveSession(
                                        UserModel(
                                            result.data.loginResult?.name.toString(),
                                            AUTH_KEY + (result.data.loginResult?.token.toString()),
                                            true
                                        )
                                    )
                                    moveActivity()
                                }
                                create()
                                show()
                            }
                        }
                        is Result.Error -> {
                            showToast()
                        }
                    }
                }
            }
        }
    }

    private fun moveActivity() {
        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
        finish()
    }

    private fun saveSession(session: UserModel){
        loginViewModel.saveSession(session)
    }

    companion object {
        private const val AUTH_KEY = "Bearer "
    }

}