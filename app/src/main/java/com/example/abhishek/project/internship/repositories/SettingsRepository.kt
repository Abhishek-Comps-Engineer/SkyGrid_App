package com.example.abhishek.project.internship.repositories

import android.content.Context
import android.net.Uri
import android.util.Log
import androidx.datastore.preferences.core.booleanPreferencesKey
import androidx.datastore.preferences.core.edit
import androidx.datastore.preferences.preferencesDataStore
import com.example.abhishek.project.internship.data.RetrofitClient
import com.example.abhishek.project.internship.model.LandDetection
import com.example.abhishek.project.internship.model.UserProfile
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.MultipartBody.Part
import okhttp3.RequestBody
import okhttp3.RequestBody.Companion.toRequestBody

val Context.dataStore by preferencesDataStore(name = "user_prefs")
class SettingsRepository(private val context: Context) {

    val TAG: String = "Settings Repository"

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
//
//    suspend fun logout() {
//        context.dataStore.edit { prefs ->
//            prefs[LOGGED_IN_KEY] = false
//        }
//    }

    suspend fun fetchProfile(email: String): Result<UserProfile> {
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

    suspend fun profileImage(email:String,imageBytes: ByteArray): Result<UserProfile> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Uploading file:, size=${imageBytes.size} bytes")

                val reqFile = imageBytes.toRequestBody("image/jpeg".toMediaTypeOrNull())
                val bodyPart = MultipartBody.Part.createFormData("image" ,"myfile.jpg",reqFile)

                // Create RequestBody for email
                val emailPart = email.toRequestBody("text/plain".toMediaTypeOrNull())

                // Call A
                val response = RetrofitClient.api.profileImage(emailPart,bodyPart)

                Log.d(TAG, "Response: $response")
                Result.success(response)

                val fullUrl = response.profileImage_url
                    ?.replace("\\", "/")
                    ?.let {
                        RetrofitClient.BASE_URL + it
                    }

                val processedResult = response.copy(profileImage_url = fullUrl.toString())

                Result.success(processedResult)

            } catch (e: Exception) {
                Log.e(TAG, "Error in profileImage", e)

                Result.failure(e)
            }
        }
    }

}
