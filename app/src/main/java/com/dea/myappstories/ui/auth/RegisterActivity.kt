package com.dea.myappstories.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import com.dea.myappstories.R
import com.dea.myappstories.databinding.ActivityRegisterBinding
import com.dea.myappstories.ui.ViewModelFactory
import com.dea.myappstories.utils.Result

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private lateinit var factory: ViewModelFactory
    private val registerViewModel: RegisterViewModel by viewModels { factory }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        supportActionBar?.hide()

        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        playAnimation()
        setupAction()
        setupViewModel()
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imageView, View.TRANSLATION_X, -30f, 30f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }.start()

        val signup = ObjectAnimator.ofFloat(binding.regisButton, View.ALPHA, 1f).setDuration(100)
        val title = ObjectAnimator.ofFloat(binding.titleTextView, View.ALPHA, 1f).setDuration(100)
        val name = ObjectAnimator.ofFloat(binding.nameEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val email = ObjectAnimator.ofFloat(binding.emailEditTextLayout, View.ALPHA, 1f).setDuration(100)
        val password = ObjectAnimator.ofFloat(binding.passwordEditTextLayout, View.ALPHA, 1f).setDuration(100)

        AnimatorSet().apply {
            playSequentially(title, name, email, password, signup)
            start()
        }
    }

    private fun setupAction() {
        binding.apply {
            if (nameEditText.length() == 0 && nameEditText.length() == 0 && passwordEditText.length() == 0) {
                nameEditText.error = getString(R.string.required_field_name)
                emailEditText.error = getString(R.string.required_field_email)
                passwordEditText.setError(getString(R.string.required_field_password), null)
                regisButton.isEnabled = false
            } else if (nameEditText.length() != 0 && emailEditText.length() != 0 && passwordEditText.length() != 0) {
                regisButton.isEnabled = true
            }

            emailEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    regisButton.isEnabled = emailEditText.text!!.isNotEmpty() && passwordEditText.text!!.isNotEmpty()
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            passwordEditText.addTextChangedListener(object : TextWatcher {
                override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
                override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                    regisButton.isEnabled = passwordEditText.length() >= 8
                }
                override fun afterTextChanged(s: Editable?) {}
            })

            regisButton.setOnClickListener {
                postText()
            }
        }
    }

    private fun setupViewModel() {
        factory = ViewModelFactory.getInstance(this)
    }

    private fun showLoading(b : Boolean) {
        binding.pbRegister.visibility = if (b) View.VISIBLE else View.GONE
    }

    private fun showToast() {
        registerViewModel.toastText.observe(this@RegisterActivity) {
            it.getContentIfNotHandled()?.let { toastText ->
                Toast.makeText(
                    this@RegisterActivity, toastText, Toast.LENGTH_SHORT
                ).show()
            }
        }
    }

    private fun moveActivity() {
        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        finish()
    }

    private fun postText() {
        binding.apply {
            registerViewModel.doRegister(
                nameEditText.text.toString(),
                emailEditText.text.toString(),
                passwordEditText.text.toString()
            ).observe(this@RegisterActivity){ result ->
                if (result != null){
                    when (result){
                        is Result.Loading -> {
                            showLoading(true)
                        }
                        is Result.Success -> {
                            showLoading(false)
                            AlertDialog.Builder(this@RegisterActivity).apply {
                                setTitle(getString(R.string.alert_title_register))
                                setMessage(getString(R.string.alert_message_register))
                                setPositiveButton(getString(R.string.alert_button_register)) { _ , _ ->
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

}