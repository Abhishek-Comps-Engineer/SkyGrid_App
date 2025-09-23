package com.example.abhishek.project.internship.model

data class ObjectDetection (
        val class_name: String,
        val confidence: Double,
        val bbox: List<List<Double>>?
)