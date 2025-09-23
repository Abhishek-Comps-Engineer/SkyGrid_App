package com.example.abhishek.project.internship.viewmodels

import android.graphics.Bitmap
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.abhishek.project.internship.model.DetectionResult
import com.example.abhishek.project.internship.repositories.UserRepository
import kotlinx.coroutines.launch


sealed class DetectionUIState {
    object Idle : DetectionUIState()
    object Loading : DetectionUIState()
    data class Success(val result: DetectionResult) : DetectionUIState()
    data class Error(val message: String) : DetectionUIState()
}

class DetectionViewModel(private val repo: UserRepository) : ViewModel() {

    private val _uiState = MutableLiveData<DetectionUIState>(DetectionUIState.Idle)
    val uiState: LiveData<DetectionUIState> get() = _uiState

    fun detectObjects(imageBytes: ByteArray, filename: String) {
        Log.d("DetectionViewModel", "detectObjects() called with filename=$filename")
        _uiState.value = DetectionUIState.Loading
        viewModelScope.launch {

            repo.detectImage(imageBytes, filename)
                .onSuccess { result ->
                    Log.d("DetectionViewModel", "Detection success: ${result.detections.size} objects")
                    _uiState.value = DetectionUIState.Success(result)
                }
                .onFailure { error ->
                    Log.e("DetectionViewModel", "Detection error", error)
                    _uiState.value = DetectionUIState.Error(error.localizedMessage ?: "Unknown error")
                }
        }
    }
}

class DetectionViewModelFactory(
    private val repo: UserRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetectionViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return DetectionViewModel(repo) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
