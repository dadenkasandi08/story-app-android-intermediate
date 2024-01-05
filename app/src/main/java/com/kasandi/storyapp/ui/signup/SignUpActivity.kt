package com.kasandi.storyapp.ui.signup

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import com.kasandi.storyapp.data.Result
import com.kasandi.storyapp.databinding.ActivitySignUpBinding
import com.kasandi.storyapp.helper.ViewModelFactory
import com.kasandi.storyapp.ui.login.LoginActivity

class SignUpActivity : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpBinding
    private lateinit var viewModel: SignUpViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpBinding.inflate(layoutInflater)
        viewModel =
            ViewModelProvider(this, ViewModelFactory.getInstance(this))[SignUpViewModel::class.java]

        setContentView(binding.root)
        playAnimation()
        initAction()
        initView()
    }

    private fun initView() {
        viewModel.getRegisterResponse().observe(this) { result ->
            if (result != null) {
                when (result) {
                    is Result.Loading -> {
                        isLoading(true)
                    }

                    is Result.Success -> {
                        Toast.makeText(this, "Account created successfully!", Toast.LENGTH_SHORT)
                            .show()
                        startActivity(Intent(this@SignUpActivity, LoginActivity::class.java))
                        finish()
                    }

                    is Result.Error -> {
                        Toast.makeText(
                            this, "Failed load data: ${result.error}!", Toast.LENGTH_SHORT
                        ).show()
                        isLoading(false)
                    }
                }
            }
        }

    }

    private fun initAction() {
        binding.btSignupSignup.setOnClickListener {
            val email = binding.edSignupEmail.text.toString().trim()
            val name = binding.edSignupUsername.text.toString().trim()
            val pass = binding.edSignupPassword.text.toString().trim()
            viewModel.register(name, email, pass)
        }
    }

    private fun isLoading(isLoading: Boolean) {
        if (isLoading) {
            binding.pbSignup.visibility = View.VISIBLE
            binding.btSignupSignup.visibility = View.INVISIBLE
        } else {
            binding.pbSignup.visibility = View.INVISIBLE
            binding.btSignupSignup.visibility = View.VISIBLE
        }
    }

    private fun playAnimation() {
        ObjectAnimator.ofFloat(binding.imSignupIllustration, View.TRANSLATION_X, -30f, 30f).apply {
                duration = 3000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }.start()

        val title = ObjectAnimator.ofFloat(binding.tvSignupTitle, View.ALPHA, 1f).setDuration(250)
        val message =
            ObjectAnimator.ofFloat(binding.tvSignupMessage, View.ALPHA, 1f).setDuration(250)
        val email = ObjectAnimator.ofFloat(binding.etlSignupEmail, View.ALPHA, 1f).setDuration(250)
        val password =
            ObjectAnimator.ofFloat(binding.etlSignupPassword, View.ALPHA, 1f).setDuration(250)
        val username =
            ObjectAnimator.ofFloat(binding.etlSignupUsername, View.ALPHA, 1f).setDuration(250)
        val signup = ObjectAnimator.ofFloat(binding.btSignupSignup, View.ALPHA, 1f).setDuration(250)

        AnimatorSet().apply {
            playSequentially(title, message, username, email, password, signup)
            start()
        }
    }
}