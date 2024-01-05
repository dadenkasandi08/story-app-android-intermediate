package com.kasandi.storyapp.ui.login

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kasandi.storyapp.data.Result
import com.kasandi.storyapp.databinding.ActivityLoginBinding
import com.kasandi.storyapp.helper.ViewModelFactory
import com.kasandi.storyapp.ui.main.MainActivity
import com.kasandi.storyapp.ui.signup.SignUpActivity

class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        viewModel =
            ViewModelProvider(this, ViewModelFactory.getInstance(this))[LoginViewModel::class.java]
        setContentView(binding.root)
        playAnimation()
        initAction()
        initView()
    }

    private fun initAction() {
        binding.btLoginSignup.setOnClickListener {
            val intentSignUp = Intent(this@LoginActivity, SignUpActivity::class.java)
            startActivity(intentSignUp)
        }

        binding.btLoginLogin.setOnClickListener {
            val email = binding.edLoginEmail.text.toString().trim()
            val pass = binding.edLoginPassword.text.toString().trim()
            viewModel.startSession(email, pass)
        }
    }

    private fun initView() {
        viewModel.getLoginResponse().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        isLoading(true)
                    }

                    is Result.Success -> {
                        viewModel.saveSession()
                        Toast.makeText(this, "Login success!", Toast.LENGTH_LONG).show()
                    }

                    is Result.Error -> {
                        Toast.makeText(this, "Login failed: ${result.error}!", Toast.LENGTH_LONG)
                            .show()
                        isLoading(false)
                    }
                }
            }
        }

        viewModel.getSession().observe(this) { user ->
            isLoading(true)
            if (user.isLogin) {
                startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                finish()
            } else {
                isLoading(false)
            }
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbLogin.visibility = View.VISIBLE
            binding.imLoginIllustration.visibility = View.INVISIBLE
            binding.tvLoginMessage.visibility = View.INVISIBLE
            binding.tvLoginTitle.visibility = View.INVISIBLE
            binding.etlLoginEmail.visibility = View.INVISIBLE
            binding.etlLoginPassword.visibility = View.INVISIBLE
            binding.btLoginLogin.visibility = View.INVISIBLE
            binding.btLoginSignup.visibility = View.INVISIBLE
        } else {
            binding.pbLogin.visibility = View.INVISIBLE
            binding.imLoginIllustration.visibility = View.VISIBLE
            binding.tvLoginMessage.visibility = View.VISIBLE
            binding.tvLoginTitle.visibility = View.VISIBLE
            binding.etlLoginEmail.visibility = View.VISIBLE
            binding.etlLoginPassword.visibility = View.VISIBLE
            binding.btLoginLogin.visibility = View.VISIBLE
            binding.btLoginSignup.visibility = View.VISIBLE
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imLoginIllustration, View.TRANSLATION_X, -30f, 30f)
            .apply {
                duration = 3000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()

        val title =
            ObjectAnimator.ofFloat(binding.tvLoginTitle, View.ALPHA, 1f).setDuration(250)
        val message =
            ObjectAnimator.ofFloat(binding.tvLoginMessage, View.ALPHA, 1f).setDuration(250)
        val email =
            ObjectAnimator.ofFloat(binding.etlLoginEmail, View.ALPHA, 1f).setDuration(250)
        val password =
            ObjectAnimator.ofFloat(binding.etlLoginPassword, View.ALPHA, 1f).setDuration(250)
        val login =
            ObjectAnimator.ofFloat(binding.btLoginLogin, View.ALPHA, 1f).setDuration(250)
        val signup =
            ObjectAnimator.ofFloat(binding.btLoginSignup, View.ALPHA, 1f).setDuration(250)

        val together = AnimatorSet().apply {
            playTogether(login, signup)
        }

        AnimatorSet().apply {
            playSequentially(title, message, email, password, together)
            start()
        }
    }
}