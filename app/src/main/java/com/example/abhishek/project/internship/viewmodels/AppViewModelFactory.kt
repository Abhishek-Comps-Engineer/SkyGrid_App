package com.example.abhishek.project.internship.viewmodels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.abhishek.project.internship.repositories.LandRepository

class DetectionViewModelFactory(
    private val repo: LandRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(LandDetectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return LandDetectionViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}