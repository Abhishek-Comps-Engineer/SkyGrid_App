package com.example.abhishek.project.internship.ui

import android.app.Activity
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.provider.OpenableColumns
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.lifecycle.ViewModelProvider
import com.example.abhishek.project.internship.R
import com.example.abhishek.project.internship.repositories.LandRepository
import com.example.abhishek.project.internship.viewmodels.AppViewModelFactory
import com.example.abhishek.project.internship.viewmodels.LandUIState
import com.example.abhishek.project.internship.viewmodels.LandViewModel
import com.google.android.material.button.MaterialButtonToggleGroup
import com.google.android.material.card.MaterialCardView
import java.io.InputStream

class LandAnalysisActivity : AppCompatActivity() {

    private lateinit var viewModel: LandViewModel

    private lateinit var placeholderView: LinearLayout
    private lateinit var progressOverlay: View
    private lateinit var imagePreview: ImageView
    private lateinit var annotatedPreview: ImageView
    private lateinit var toggleGroup: MaterialButtonToggleGroup
    private lateinit var resultsDashboard: LinearLayout
    private lateinit var predictedClass: TextView
    private lateinit var confidenceScore: TextView
    private lateinit var suggestionsContainer: LinearLayout
    private lateinit var btnChangeImage: Button
    private lateinit var btnAnalyzeLand: Button

    private var selectedImageBytes: ByteArray? = null
    private var selectedFilename: String = "land_image.jpg"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_land_analysis)
        val repo = LandRepository()

        viewModel = ViewModelProvider(
            this,
            AppViewModelFactory {
                LandViewModel(repo)
            }
        ).get(LandViewModel::class.java)

        placeholderView = findViewById(R.id.placeholder_view)
        progressOverlay = findViewById(R.id.progress_overlay)
        imagePreview = findViewById(R.id.image_preview)
        annotatedPreview = findViewById(R.id.annotated_preview)
        toggleGroup = findViewById(R.id.toggle_button_group)
        resultsDashboard = findViewById(R.id.results_dashboard)
        predictedClass = findViewById(R.id.predicted_class)
        confidenceScore = findViewById(R.id.confidence_score)
        suggestionsContainer = findViewById(R.id.suggestions_container)
        btnChangeImage = findViewById(R.id.btn_change_image)
        btnAnalyzeLand = findViewById(R.id.btn_analyze_land)

        val imagePicker =
            registerForActivityResult(ActivityResultContracts.StartActivityForResult()) { result ->
                if (result.resultCode == Activity.RESULT_OK) {
                    result.data?.data?.let { uri ->
                        handleImageSelected(uri)
                    }
                }
            }

        placeholderView.setOnClickListener {
            pickImage(imagePicker)
        }
        btnChangeImage.setOnClickListener {
            pickImage(imagePicker)
        }

        btnAnalyzeLand.setOnClickListener {
            selectedImageBytes?.let { bytes ->
                viewModel.detectLand(bytes, selectedFilename)
            }
            progressOverlay.isVisible = true
        }

        toggleGroup.addOnButtonCheckedListener { _, checkedId, isChecked ->
            if (isChecked) {
                when (checkedId) {
                    R.id.btn_toggle_original -> {
                        imagePreview.isVisible = true
                        annotatedPreview.isVisible = false
                    }
                    R.id.btn_toggle_annotated -> {
                        imagePreview.isVisible = false
                        annotatedPreview.isVisible = true
                    }
                }
            }
        }

        observeViewModel()
    }

    private fun observeViewModel() {
        viewModel.uiState.observe(this) { state ->
            when (state) {
                is LandUIState.Idle -> {
                    progressOverlay.visibility = View.GONE
                }
                is LandUIState.Loading -> {
                    progressOverlay.visibility = View.VISIBLE
                }
                is LandUIState.Success -> {
                    progressOverlay.visibility = View.GONE
                    resultsDashboard.visibility = View.VISIBLE
                    toggleGroup.visibility = View.VISIBLE

                    predictedClass.text = state.result.predicted_class
                    confidenceScore.text = "${"%.2f".format(state.result.confidence * 100)}%"

                    suggestionsContainer.removeAllViews()
                    state.result.suggested_use_cases.distinct().forEach { suggestion ->
                        val tv = TextView(this).apply {
                            text = "• $suggestion"
                            textSize = 16f
                            setTextColor(resources.getColor(R.color.text_color_primary, theme))
                        }
                        suggestionsContainer.addView(tv)
                    }

                    Log.d("LandAnalysis", "Analysis success: ${state.result.predicted_class}")
                }
                is LandUIState.Error -> {
                    progressOverlay.visibility = View.GONE
                    resultsDashboard.visibility = View.GONE
                    toggleGroup.visibility = View.GONE
                    Log.e("LandAnalysis", "Error: ${state.message}")
                }
            }
        }
    }

    private fun pickImage(imagePicker: androidx.activity.result.ActivityResultLauncher<Intent>) {
        val intent = Intent(Intent.ACTION_PICK).apply {
            type = "image/*"
        }
        imagePicker.launch(intent)
    }

    private fun handleImageSelected(uri: Uri) {
        try {
            val inputStream: InputStream? = contentResolver.openInputStream(uri)
            selectedImageBytes = inputStream?.readBytes()
            selectedFilename = getFileName(uri) ?: "land_image.jpg"

            // Preview original image
            val bitmap = BitmapFactory.decodeByteArray(selectedImageBytes, 0, selectedImageBytes!!.size)
            imagePreview.setImageBitmap(bitmap)
            annotatedPreview.setImageBitmap(bitmap)

            placeholderView.visibility = View.GONE
            imagePreview.visibility = View.VISIBLE
            toggleGroup.visibility = View.VISIBLE
            resultsDashboard.visibility = View.GONE

        } catch (e: Exception) {
            Log.e("LandAnalysis", "Image handling error", e)
        }
    }

    private fun getFileName(uri: Uri): String? {
        var name: String? = null
        contentResolver.query(uri, null, null, null, null)?.use { cursor ->
            val nameIndex = cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)
            if (cursor.moveToFirst()) {
                name = cursor.getString(nameIndex)
            }
        }
        return name
    }
}
