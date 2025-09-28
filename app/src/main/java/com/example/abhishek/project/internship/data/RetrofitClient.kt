package com.example.abhishek.project.internship.data

import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {
        internal const val BASE_URL = "http://10.0.2.2:8000/"

    fun apiBaseUrl() = BASE_URL


    private val logging = HttpLoggingInterceptor().apply {
            level = HttpLoggingInterceptor.Level.BODY
        }
        private val client = OkHttpClient.Builder()
            .addInterceptor(logging)
            .build()


    val gson = GsonBuilder()
        .setDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSS") // matches '2025-09-28T04:45:15.784798'
        .create()

    private val retrofit: Retrofit by lazy {
            Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(gson))
                .build()
        }

        val api: ApiServices by lazy {
            retrofit.create(ApiServices::class.java)
        }
}

