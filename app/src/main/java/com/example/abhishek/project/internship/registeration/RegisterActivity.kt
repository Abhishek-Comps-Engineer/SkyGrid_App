package com.example.abhishek.project.internship.registeration

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.example.abhishek.project.internship.ui.MainActivity
import com.example.abhishek.project.internship.databinding.ActivityRegisterBinding
import com.example.abhishek.project.internship.viewmodels.RegisterViewModel

class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel: RegisterViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupListeners()
    }

    private fun setupListeners() {

        binding.createAccountButton.setOnClickListener {
            val fullName = binding.fullnameEdittext.text.toString().trim()
            val email = binding.emailEdittext.text.toString().trim()
            val password = binding.passwordEdittext.text.toString().trim()
            val termsChecked = binding.termsCheckbox.isChecked

            if (fullName.isEmpty() || email.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "Please fill all fields", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            if (!viewModel.isStrongPassword(password)) {
                Toast.makeText(this, "Password must be at least 8 characters, include uppercase, lowercase, digit & special character", Toast.LENGTH_LONG).show()
                return@setOnClickListener
            }
            if (!termsChecked) {
                Toast.makeText(this, "Please accept Terms & Conditions", Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.register(fullName, email, password).observe(this) { success ->
                if (success) {
                    Toast.makeText(this, "Registration Successful", Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)
                    finish()
                } else {
                    Toast.makeText(this, "Registration Failed. Check your inputs.", Toast.LENGTH_SHORT).show()
                }
            }
        }

        binding.loginPromptText.setOnClickListener {
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
            finish()
        }
    }
}
