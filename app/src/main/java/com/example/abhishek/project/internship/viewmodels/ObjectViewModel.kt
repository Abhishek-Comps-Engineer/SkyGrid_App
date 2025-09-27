package com.example.abhishek.project.internship.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.abhishek.project.internship.model.DetectionResult
import com.example.abhishek.project.internship.repositories.ObjectRepository
import kotlinx.coroutines.launch




class DetectionViewModel(private val repo: ObjectRepository) : ViewModel() {

    private val _uiState = MutableLiveData<ObjectDetectionUIState>(ObjectDetectionUIState.Idle)
    val uiState: LiveData<ObjectDetectionUIState> get() = _uiState

    fun detectObjects(imageBytes: ByteArray, filename: String) {
        Log.d("DetectionViewModel", "detectObjects() called with filename=$filename")
        _uiState.value = ObjectDetectionUIState.Loading
        viewModelScope.launch {

            repo.detectImage(imageBytes, filename)
                .onSuccess { result ->
                    Log.d("DetectionViewModel", "Detection success: ${result.detections.size} objects")
                    _uiState.value = ObjectDetectionUIState.Success(result)
                }
                .onFailure { error ->
                    Log.e("DetectionViewModel", "Detection error", error)
                    _uiState.value = ObjectDetectionUIState.Error(error.localizedMessage ?: "Unknown error")
                }
        }
    }
}

sealed class ObjectDetectionUIState {
    object Idle : ObjectDetectionUIState()
    object Loading : ObjectDetectionUIState()
    data class Success(val result: DetectionResult) : ObjectDetectionUIState()
    data class Error(val message: String) : ObjectDetectionUIState()
}