package com.example.abhishek.project.internship.model

data class ObjectDetection (
        val class_name: String,
        val confidence: Double,
        val bbox: List<List<Double>>?
)

data class DetectionResult(
        val detections: List<ObjectDetection> = emptyList(),
        val result_image_url: String? = null
)