package com.example.abhishek.project.internship.registeration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.abhishek.project.internship.ui.MainActivity
import com.example.abhishek.project.internship.databinding.ActivityLoginBinding
import com.example.abhishek.project.internship.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private val viewModel: LoginViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel.isLoggedIn.observe(this) { loggedIn ->
            if (loggedIn) {
                startActivity(Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                })
            }
        }

        setupListeners()

    }
    private fun setupListeners() {

        binding.loginButton.setOnClickListener {
            val email = binding.emailEdittext.text.toString().trim()
            val password = binding.passwordEdittext.text.toString().trim()

            if (email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please enter email & password", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.login(email, password)
            viewModel.loginSuccess.observe(this) { success ->
                if (success) {
                    startActivity(Intent(this, MainActivity::class.java).apply {
                        flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    })
                    finish()
                } else {
                    Toast.makeText(this, "Invalid email or password", Toast.LENGTH_SHORT).show()
                }
            }
        }


//        binding.googleButton.setOnClickListener {
//            viewModel.loginWithGoogle().observe(this, Observer { success ->
//                if (success) {
//                    Toast.makeText(this, "Google Sign-In Successful", Toast.LENGTH_SHORT).show()
//                } else {
//                    Toast.makeText(this, "Google Sign-In Failed", Toast.LENGTH_SHORT).show()
//                }
//            })
//        }


        binding.signupPromptText.setOnClickListener {
            val intentRegister = Intent(this@LoginActivity, RegisterActivity::class.java)
            startActivity(intentRegister)
        }


        binding.forgotPasswordText.setOnClickListener {
            Toast.makeText(this, "Navigate to Forgot Password Screen", Toast.LENGTH_SHORT).show()
        }
    }
}