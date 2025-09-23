package com.example.abhishek.project.internship.data

import com.example.abhishek.project.internship.model.DetectionResult
import okhttp3.MultipartBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface ApiServices {
    @Multipart
    @POST("detect/")
    suspend fun detectObjects(
        @Part file: MultipartBody.Part
    ): DetectionResult
}