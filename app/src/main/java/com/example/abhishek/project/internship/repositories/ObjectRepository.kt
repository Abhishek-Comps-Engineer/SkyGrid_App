package com.example.abhishek.project.internship.repositories

import android.util.Log
import com.example.abhishek.project.internship.data.RetrofitClient
import com.example.abhishek.project.internship.model.DetectionResult
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody.*
import okhttp3.RequestBody

class ObjectRepository {

//    fun updateProfile(name: String, email: String, onResult: (Boolean) -> Unit) {
//        firebaseHelper.updateUserProfile(name, email, onResult)
//    }
//
//    fun uploadProfileImage(uri: Uri, onResult: (Boolean, String?) -> Unit) {
//        firebaseHelper.uploadProfileImage(uri, onResult)
//    }
    suspend fun detectImage(imageBytes: ByteArray, filename: String): Result<DetectionResult> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d("UserRepository", "Uploading file: $filename, size=${imageBytes.size} bytes")

                val reqFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageBytes)
                val body = Part.createFormData("file", filename, reqFile)
                val response = RetrofitClient.api.detectObjects(body)

                Log.d("UserRepository", "Response: $response")
                Result.success(response)

                val fullUrl = response.result_image_url
                    ?.replace("\\", "/")
                    ?.let {
                        RetrofitClient.BASE_URL + it
                    }

                val processedResult = response.copy(result_image_url = fullUrl)

                Result.success(processedResult)

            } catch (e: Exception) {
                Log.e("UserRepository", "Error in detectImage", e)

                Result.failure(e)
            }
        }
    }
}
