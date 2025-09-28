package com.example.abhishek.project.internship.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.abhishek.project.internship.model.History
import com.example.abhishek.project.internship.repositories.HistoryRepository
import kotlinx.coroutines.launch

class HistoryViewModel(private val repository: HistoryRepository) : ViewModel() {

    private val _history = MutableLiveData<List<History>>()
    val history: LiveData<List<History>> = _history

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _error = MutableLiveData<String>()
    val error: LiveData<String> = _error

    fun loadHistory(page: Int = 1, limit: Int = 20) {
        viewModelScope.launch {
            _isLoading.value = true
            val result = repository.getHistory(page, limit)
            if (result.isSuccess) {
                _history.value = result.getOrNull()
            } else {
                _error.value = result.exceptionOrNull()?.localizedMessage ?: "Unknown error"
            }
            _isLoading.value = false
        }
    }
}
