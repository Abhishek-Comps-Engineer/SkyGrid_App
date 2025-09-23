package com.example.abhishek.project.internship.repositories

import android.content.Context
import android.net.Uri
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

val Context.dataStore by preferencesDataStore(name = "user_prefs")

class SettingsRepository(private val context: Context) {

    private val LOGGED_IN_KEY = booleanPreferencesKey("logged_in")
    private val DARK_MODE_KEY = booleanPreferencesKey("dark_mode")

    val isLoggedIn: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[LOGGED_IN_KEY] ?: false }

    val darkModeFlow: Flow<Boolean> = context.dataStore.data
        .map { prefs -> prefs[DARK_MODE_KEY] ?: false }

    suspend fun setLoggedIn(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN_KEY] = enabled
        }
    }

    suspend fun setDarkMode(enabled: Boolean) {
        context.dataStore.edit { prefs ->
            prefs[DARK_MODE_KEY] = enabled
        }
    }

    suspend fun logout() {
        context.dataStore.edit { prefs ->
            prefs[LOGGED_IN_KEY] = false
        }
    }
}
