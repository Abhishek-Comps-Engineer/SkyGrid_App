package com.example.abhishek.project.internship.model

import com.google.gson.annotations.SerializedName
import java.util.*

data class History(
    val id: Int,
    val filename: String,
    @SerializedName("activity_detail") val activityDetail: String,
    @SerializedName("created_at") val createdAt: Date,
    @SerializedName("activity_type") val activityType: String,
    var imageUrl: String? = null

)

data class HistoryResponse(
    val activities: List<History>
)
