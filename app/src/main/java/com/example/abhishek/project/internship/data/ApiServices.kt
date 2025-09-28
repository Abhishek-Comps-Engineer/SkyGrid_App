package com.example.abhishek.project.internship.data

import com.example.abhishek.project.internship.model.DetectionResult
import com.example.abhishek.project.internship.model.HistoryResponse
import com.example.abhishek.project.internship.model.LandDetection
import com.example.abhishek.project.internship.model.UserProfile
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part
import retrofit2.http.Query

interface ApiServices {
    @Multipart
    @POST("detect/")
    suspend fun detectObjects(
        @Part file: MultipartBody.Part
    ): DetectionResult


    @Multipart
    @POST("land/")
    suspend fun detectLands(
        @Part file : MultipartBody.Part
    ): LandDetection


    @Multipart
    @POST("user/")
    suspend fun profileImage(
        @Part("email") email : RequestBody,
        @Part file : MultipartBody.Part
    ): UserProfile


    @GET("user/")
    suspend fun getProfile(
        @Query("email") email: String
    ): UserProfile


    @GET("history/")
    suspend fun getRecentHistories(
        @Query("page") page: Int = 1,
        @Query("limit") limit: Int = 20
    ): HistoryResponse


}