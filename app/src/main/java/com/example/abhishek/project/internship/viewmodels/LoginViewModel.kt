package com.example.abhishek.project.internship.viewmodels
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import com.example.abhishek.project.internship.repositories.FirebaseHelper
import com.example.abhishek.project.internship.repositories.SettingsRepository
import kotlinx.coroutines.launch

class LoginViewModel(application: Application) : AndroidViewModel(application) {

    private val repository = FirebaseHelper.getInstance()
    private val repositorySettings = SettingsRepository(application)
    private val _loginSuccess = MutableLiveData<Boolean>()
    val loginSuccess: LiveData<Boolean> = _loginSuccess

    fun login(email: String, password: String) {
        repository.login(email, password).observeForever { success ->
            if (success) {
                // Save session in DataStore
                viewModelScope.launch {
                    repositorySettings.setLoggedIn(true)
                }
                _loginSuccess.value = true
            } else {
                _loginSuccess.value = false
            }
        }
    }
    fun loginWithGoogle(idToken: String): LiveData<Boolean> {
        return repository.loginWithGoogle(idToken)
    }

    val isLoggedIn: LiveData<Boolean> = repositorySettings.isLoggedIn.asLiveData()

}





