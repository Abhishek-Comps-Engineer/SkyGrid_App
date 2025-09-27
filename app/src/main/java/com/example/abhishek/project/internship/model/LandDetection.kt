package com.example.abhishek.project.internship.model

data class LandDetection (
    var predicted_class : String,
    var confidence: Double,
    var suggested_use_cases: List<String>,
    var annotated_image_url: String
)