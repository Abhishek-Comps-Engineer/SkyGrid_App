package com.example.abhishek.project.internship.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import com.example.abhishek.project.internship.model.DetectionResult
import com.example.abhishek.project.internship.model.LandDetection
import com.example.abhishek.project.internship.repositories.LandRepository
import com.example.abhishek.project.internship.repositories.ObjectRepository
import kotlinx.coroutines.launch
const val TAG: String = "LandDetectionViewModel"





class LandDetectionViewModel(private val repo: LandRepository) : ViewModel() {

    private val _uiState = MutableLiveData<DetectionUIState>(DetectionUIState.Idle)
    val uiState: LiveData<DetectionUIState> get() = _uiState

    fun detectObjects(imageBytes: ByteArray, filename: String) {
        Log.d(TAG, "detectLands() called with filename=$filename")
        _uiState.value = DetectionUIState.Loading
        viewModelScope.launch {

            repo.detectImage(imageBytes, filename)
                .onSuccess { result ->
                    Log.d(TAG, "Detection success: ${result.predicted_class}")
                    _uiState.value = DetectionUIState.Success(result)
                }
                .onFailure { error ->
                    Log.e("DetectionViewModel", "Detection error", error)
                    _uiState.value = DetectionUIState.Error(error.localizedMessage ?: "Unknown error")
                }
        }
    }
}


sealed class LandUIState {
    object Idle : LandUIState()
    object Loading : LandUIState()
    data class Success(val result: LandDetection) : LandUIState()
    data class Error(val message: String) : LandUIState()
}

