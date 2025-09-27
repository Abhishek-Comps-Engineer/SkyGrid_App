package com.example.abhishek.project.internship.viewmodels

import android.app.Application
import android.util.Log
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.abhishek.project.internship.model.UserProfile
import com.example.abhishek.project.internship.repositories.FirebaseHelper
import com.example.abhishek.project.internship.repositories.SettingsRepository
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class SettingsViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = SettingsRepository(application)
    private val repositoryAuth = FirebaseHelper.getInstance()
    private val _profileImageUrl = MutableLiveData<String?>()
    val profileImageUrl: LiveData<String?> = _profileImageUrl
    private val _action = MutableLiveData<String>()
    val action: LiveData<String> get() = _action




    val darkMode: StateFlow<Boolean> = repository.darkModeFlow
        .stateIn(viewModelScope, SharingStarted.Lazily, false)

    private val _uiState = MutableLiveData<SettingsUIState>(SettingsUIState.Idle)
    val uiState: LiveData<SettingsUIState> get() = _uiState

    fun uploadProfileImage(email:String = repositoryAuth.getCurrentUserEmail(),
                           imageBytes: ByteArray,
                           filename: String
    ) {
        Log.d(TAG, "detectLands() called with filename=$filename")
        _uiState.value = SettingsUIState.Loading
        viewModelScope.launch {

            repository.profileImage(email,imageBytes, filename)
                .onSuccess { result ->
                    Log.d(TAG, "Profile upload success: ${result.profileImage_url}")
                    _uiState.value = SettingsUIState.Success(result)
                }
                .onFailure { error ->
                    _uiState.value = SettingsUIState.Error(error.localizedMessage ?: "Unknown error")
                }
        }
    }
    fun getUserName() = repositoryAuth.getCurrentUserName()
    fun getUserEmail() = repositoryAuth.getCurrentUserEmail()


    fun setDarkMode(enabled: Boolean) {
        viewModelScope.launch {
            repository.setDarkMode(enabled)
        }
    }

    suspend fun logout() {
        repository.setLoggedIn(false)
        repositoryAuth.logoutAuth()
    }

    fun onEditProfileClicked() {
        _action.value = "Edit Profile"
    }

    fun onChangePasswordClicked() {
        _action.value = "Change Password"
    }


    fun helpClicked() {
        _action.value = "Help"
    }


}

sealed class SettingsUIState {
    object Idle : SettingsUIState()
    object Loading : SettingsUIState()
    data class Success(val result: UserProfile) : SettingsUIState()
    data class Error(val message: String) : SettingsUIState()
}
