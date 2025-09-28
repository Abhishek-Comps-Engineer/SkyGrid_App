package com.example.abhishek.project.internship.repositories

import android.util.Log
import com.example.abhishek.project.internship.data.RetrofitClient
import com.example.abhishek.project.internship.model.History

class HistoryRepository {

    suspend fun getHistory(page: Int, limit: Int): Result<List<History>> {
        return try {
            val response = RetrofitClient.api.getRecentHistories(page, limit)
            val list = response.activities.map { history ->
                history.copy(
                    imageUrl = RetrofitClient.apiBaseUrl() + "results/" + history.filename
                )
            }
            Log.d(TAG, "Fetched ${list} history items")
            Result.success(list)
        } catch (e: Exception) {
            Log.e(TAG, "Error fetching history", e)
            Result.failure(e)
        }
    }
}

