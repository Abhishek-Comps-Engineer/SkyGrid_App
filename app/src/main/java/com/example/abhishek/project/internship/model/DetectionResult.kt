package com.example.abhishek.project.internship.model


data class DetectionResult(
    val detections: List<ObjectDetection> = emptyList(),
    val result_image_url: String? = null
)