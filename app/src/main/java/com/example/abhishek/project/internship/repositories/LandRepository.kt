package com.example.abhishek.project.internship.repositories

import android.util.Log
import com.example.abhishek.project.internship.data.RetrofitClient
import com.example.abhishek.project.internship.model.DetectionResult
import com.example.abhishek.project.internship.model.LandDetection
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody.Part
import okhttp3.RequestBody
const val TAG : String = "LandRepository"

class LandRepository {

    suspend fun detectImage(imageBytes: ByteArray, filename: String): Result<LandDetection> {
        return withContext(Dispatchers.IO) {
            try {
                Log.d(TAG, "Uploading file: $filename, size=${imageBytes.size} bytes")

                val reqFile = RequestBody.create("image/jpeg".toMediaTypeOrNull(), imageBytes)
                val body = Part.createFormData("file", filename, reqFile)
                val response = RetrofitClient.api.detectLands(body)

                Log.d(TAG, "Response: $response")
                Result.success(response)

                val fullUrl = response.annotated_image_url
                    ?.replace("\\", "/")
                    ?.let {
                        RetrofitClient.BASE_URL + it
                    }

                val processedResult = response.copy(annotated_image_url = fullUrl.toString())

                Result.success(processedResult)

            } catch (e: Exception) {
                Log.e(TAG, "Error in detectImage", e)

                Result.failure(e)
            }
        }
    }

}