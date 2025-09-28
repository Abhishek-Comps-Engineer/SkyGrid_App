package com.example.abhishek.project.internship.viewmodels

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.abhishek.project.internship.R
import com.example.abhishek.project.internship.data.RetrofitClient
import com.example.abhishek.project.internship.model.RecentActivity
import com.example.abhishek.project.internship.model.UserProfile
import com.example.abhishek.project.internship.repositories.FirebaseHelper
import com.example.abhishek.project.internship.repositories.SettingsRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext


class HomeViewModel : ViewModel() {
    private val repositoryAuth = FirebaseHelper.getInstance()
    private val _userName = MutableLiveData(
        getUserName()
    )
    val userName: LiveData<String> = _userName

    private val _recentActivities = MutableLiveData<List<RecentActivity>>()
    val recentActivities: LiveData<List<RecentActivity>> = _recentActivities

    private val _uiState = MutableLiveData<HomeUIState>(HomeUIState.Idle)
    val uiState: LiveData<HomeUIState> get() = _uiState

    fun fetchProfile(email: String) {
        _uiState.value = HomeUIState.Loading

        viewModelScope.launch {
            val result = fetchProfileImage(email)
            result.fold(
                onSuccess = { _uiState.value = HomeUIState.Success(it) },
                onFailure = { _uiState.value = HomeUIState.Error(it.localizedMessage ?: "Unknown error") }
            )
        }
    }

    fun getUserName() = repositoryAuth.getCurrentUserName()
    fun getUserEmail() = repositoryAuth.getCurrentUserEmail()


    suspend fun fetchProfileImage(email: String): Result<UserProfile> {
        return withContext(Dispatchers.IO) {
            try {
                val response = RetrofitClient.api.getProfile(email) // define GET endpoint
                val fullUrl = response.profileImage_url
                    ?.replace("\\", "/")
                    ?.let { RetrofitClient.BASE_URL + it }

                val processedResponse = response.copy(profileImage_url = fullUrl.toString())
                Result.success(processedResponse)
            } catch (e: Exception) {
                Log.e(TAG, "Error fetching profile", e)
                Result.failure(e)
            }
        }
    }


    init {
        loadRecentActivities()
    }

    private fun loadRecentActivities() {
        _recentActivities.value = listOf(
            RecentActivity("Crop Health", "Aug 08, 2025", R.drawable.ic_crop_analysis, R.color.green_healthy),
            RecentActivity("Flood Map", "Aug 07, 2025", R.drawable.ic_flood, R.color.colorAccent),
            RecentActivity("Road Survey", "Aug 05, 2025", R.drawable.ic_road, R.color.red_danger)
        )
    }
}


sealed class HomeUIState {
    object Idle : HomeUIState()
    object Loading : HomeUIState()
    data class Success(val result: UserProfile) : HomeUIState()
    data class Error(val message: String) : HomeUIState()
}