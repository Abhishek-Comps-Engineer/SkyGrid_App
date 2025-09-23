package com.example.abhishek.project.internship.viewmodels

import android.app.Application
import android.net.Uri
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
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

    fun uploadProfileImage(uri: Uri) {
        repositoryAuth.uploadProfileImage(uri) { success, url ->
            if (success && !url.isNullOrEmpty()) {
                _profileImageUrl.postValue(url)
            }
        }

        fun getProfileImageUrl(callback: (String?) -> Unit) {
            repositoryAuth.getProfileImageUrl(callback)
        }

    }

}
