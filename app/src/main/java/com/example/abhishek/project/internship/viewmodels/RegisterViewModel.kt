package com.example.abhishek.project.internship.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.abhishek.project.internship.repositories.FirebaseHelper

class RegisterViewModel : ViewModel() {

    private val repository = FirebaseHelper.getInstance()

    fun register(fullName: String, email: String, password: String): LiveData<Boolean> {
        return repository.register(fullName, email, password)
    }
    fun isStrongPassword(password: String): Boolean {
        val passwordPattern = Regex("^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=.*[@#\$%^&+=!])(?=\\S+\$).{8,}\$")
        return passwordPattern.matches(password)
    }
}
