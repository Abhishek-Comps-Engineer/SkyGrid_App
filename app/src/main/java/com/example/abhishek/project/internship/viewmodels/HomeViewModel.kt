package com.example.abhishek.project.internship.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.abhishek.project.internship.R
import com.example.abhishek.project.internship.model.RecentActivity


class HomeViewModel : ViewModel() {

    private val _userName = MutableLiveData("Abhishek!")
    val userName: LiveData<String> = _userName

    private val _recentActivities = MutableLiveData<List<RecentActivity>>()
    val recentActivities: LiveData<List<RecentActivity>> = _recentActivities

    init {
        loadRecentActivities()
    }

    private fun loadRecentActivities() {
        _recentActivities.value = listOf(
            RecentActivity("Crop Health", "Aug 08, 2025", R.drawable.ic_crop_analysis, R.color.green_healthy),
            RecentActivity("Flood Map", "Aug 07, 2025", R.drawable.ic_flood, R.color.colorAccent),
            RecentActivity("Road Survey", "Aug 05, 2025", R.drawable.ic_road, R.color.red_danger)
        )
    }
}
