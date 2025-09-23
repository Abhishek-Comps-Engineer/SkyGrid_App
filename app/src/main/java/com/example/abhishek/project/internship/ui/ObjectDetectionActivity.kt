package com.example.abhishek.project.internship.ui

import android.graphics.Bitmap
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import com.bumptech.glide.Glide
import com.example.abhishek.project.internship.R
import com.example.abhishek.project.internship.model.ObjectDetection
import com.example.abhishek.project.internship.repositories.UserRepository
import com.example.abhishek.project.internship.viewmodels.DetectionUIState
import com.example.abhishek.project.internship.viewmodels.DetectionViewModel
import com.example.abhishek.project.internship.viewmodels.DetectionViewModelFactory
import com.google.android.material.button.MaterialButtonToggleGroup

class ObjectDetectionActivity : AppCompatActivity() {

    private lateinit var imagePreview: ImageView
    private lateinit var imagePlaceholder: LinearLayout
    private lateinit var imageResultOverlay: ImageView
    private lateinit var placeholderView: View
    private lateinit var progressOverlay: View
    private lateinit var detectedLinearLayout : LinearLayout
    private lateinit var summaryText: TextView
    private lateinit var detectedItemsContainer: LinearLayout
    private lateinit var btnDetect: Button
    private lateinit var toggleGroup: MaterialButtonToggleGroup

    private var originalBitmap: Bitmap? = null

    private val viewModel: DetectionViewModel by viewModels {
        DetectionViewModelFactory(UserRepository())
    }

    private val pickImage = registerForActivityResult(ActivityResultContracts.GetContent()) { uri: Uri? ->
        Log.d("ObjectDetectionActivity", "Image Picked")
        uri?.let { onImagePicked(it) }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_object_detection)

        imagePreview = findViewById(R.id.image_preview)
        imagePlaceholder = findViewById(R.id.placeholder_view)
        imageResultOverlay = findViewById(R.id.image_result_overlay)
        placeholderView = findViewById(R.id.placeholder_view)
        progressOverlay = findViewById(R.id.progress_overlay)
        summaryText = findViewById(R.id.summary_text)
        detectedItemsContainer = findViewById(R.id.detected_items_container)
        detectedLinearLayout = findViewById<LinearLayout>(R.id.results_dashboard)
        btnDetect = findViewById(R.id.btn_detect_objects)
        toggleGroup = findViewById(R.id.toggle_button_group)

        toggleGroup.addOnButtonCheckedListener { group, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_toggle_result -> {
                        imagePreview.isVisible = false
                        imageResultOverlay.isVisible = true
                        detectedLinearLayout.isActivated = true
                        detectedLinearLayout.isVisible = true
                    }
                    R.id.btn_toggle_original -> {
                        imagePreview.isVisible = true
                        imageResultOverlay.isVisible = false
                        detectedLinearLayout.isActivated = false
                        detectedLinearLayout.isVisible = false
                    }
                }
            }
        }

        findViewById<Button>(R.id.btn_change_image).setOnClickListener {
            pickImage.launch("image/*")
        }

        imagePlaceholder.setOnClickListener {
            pickImage.launch("image/*")
        }
        btnDetect.setOnClickListener { detectObjects() }

        observeViewModel()
    }

    private fun onImagePicked(uri: Uri) {
        placeholderView.visibility = View.GONE
        imagePreview.visibility = View.VISIBLE
        imageResultOverlay.visibility = View.GONE

        Glide.with(this)
            .asBitmap()
            .load(uri)
            .into(object : com.bumptech.glide.request.target.BitmapImageViewTarget(imagePreview) {
                override fun setResource(resource: Bitmap?) {
                    super.setResource(resource)
                    originalBitmap = resource
                    Log.d("ObjectDetectionActivity", "Image Loaded on UI, Bitmap set")
                }
            })
    }


    private fun detectObjects() {
        originalBitmap?.let {
            detectedLinearLayout.isActivated = true
            detectedLinearLayout.isVisible = true
            val baos = java.io.ByteArrayOutputStream()
            it.compress(Bitmap.CompressFormat.JPEG, 90, baos)
            viewModel.detectObjects(baos.toByteArray(), "upload.jpg")
        } ?: run {
            Toast.makeText(this, "Please select an image first", Toast.LENGTH_SHORT).show()
        }
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is DetectionUIState.Idle -> progressOverlay.isVisible = false
                is DetectionUIState.Loading -> progressOverlay.isVisible = true
                is DetectionUIState.Success -> {
                    progressOverlay.isVisible = false
                    toggleGroup.isVisible = true
                    summaryText.text = "Found ${state.result.detections.size} objects"
                    showDetections(state.result.detections)
                    state.result.result_image_url?.let { fullUrl ->
                        Glide.with(this)
                            .load(fullUrl)
                            .error(R.drawable.ic_profile)
                            .into(imageResultOverlay)
                        imageResultOverlay.isVisible = true
                    }

                }
                is DetectionUIState.Error -> {
                    progressOverlay.isVisible = false
                    Toast.makeText(this, state.message, Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    private fun showDetections(detections: List<ObjectDetection>) {

        // Update summary
        summaryText.text = "Found ${detections.size} object(s)"

        // Clear previous items
        detectedItemsContainer.removeAllViews()

        // Group detections by class name
        val grouped = detections.groupBy { it.class_name }

        grouped.forEach { (category, items) ->

            // Inflate category header from your layout
            val categoryHeader = layoutInflater.inflate(R.layout.list_item_detected_object, detectedItemsContainer, false)
            val label = categoryHeader.findViewById<TextView>(R.id.item_label)
            val count = categoryHeader.findViewById<TextView>(R.id.item_count)

            label.text = category
            count.text = "${items.size} detected"
            // Instead of icon, we can hide it or reuse to show first score

            detectedItemsContainer.addView(categoryHeader)

            // Add each detection with score
            items.forEach { detection ->
                val itemView = layoutInflater.inflate(R.layout.list_item_detected_object, detectedItemsContainer, false)
                val labelScore = itemView.findViewById<TextView>(R.id.item_label)
                val scoreText = itemView.findViewById<TextView>(R.id.item_count)

                labelScore.text = "Confidence"
                scoreText.text = "%.2f".format(detection.confidence)

                detectedItemsContainer.addView(itemView)
            }
        }
    }
}
