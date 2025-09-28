package com.example.abhishek.project.internship.fragments

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abhishek.project.internship.R
import com.example.abhishek.project.internship.adapters.RecentActivityAdapter
import com.example.abhishek.project.internship.ui.FloodActivity
import com.example.abhishek.project.internship.ui.LandAnalysisActivity
import com.example.abhishek.project.internship.ui.ObjectActivity
import com.example.abhishek.project.internship.viewmodels.HomeUIState
import com.example.abhishek.project.internship.viewmodels.HomeViewModel
import com.google.android.material.card.MaterialCardView
import de.hdodenhof.circleimageview.CircleImageView


class HomeFragment : Fragment() {

    private val viewModel: HomeViewModel by viewModels()
    private lateinit var adapter: RecentActivityAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        val userNameText = view.findViewById<TextView>(R.id.user_name_text)
        val profileImage = view.findViewById<CircleImageView>(R.id.profile_image)
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_recent)
        val cardFeaturedDetection = view.findViewById<MaterialCardView>(R.id.card_featured_action)
        val cardObjectDetection = view.findViewById<MaterialCardView>(R.id.card_object_detection)
        val cardLandAnalysis = view.findViewById<MaterialCardView>(R.id.card_land_analysis)
        val cardFloodDetection = view.findViewById<MaterialCardView>(R.id.card_flood_detection)
        val cardRoadSurvey = view.findViewById<MaterialCardView>(R.id.card_road_survey)


        cardFeaturedDetection.setOnClickListener {
            pickImageLauncher.launch("image/*")
        }

        adapter = RecentActivityAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        viewModel.userName.observe(viewLifecycleOwner) { userName ->
            userNameText.text = userName
        }

        viewModel.recentActivities.observe(viewLifecycleOwner) { activities ->
            adapter.updateList(activities)
        }

        cardObjectDetection.setOnClickListener {
            openActivity(ObjectActivity::class.java, mapOf("feature_name" to "Object Detection"))
        }

        cardLandAnalysis.setOnClickListener {
            openActivity(LandAnalysisActivity::class.java, mapOf("feature_name" to "Land Analysis"))
        }

        cardFloodDetection.setOnClickListener {
//            openActivity(FloodActivity::class.java, mapOf("feature_name" to "Flood Detection"))
        }

        cardRoadSurvey.setOnClickListener {
//            openActivity(PotholeActivity::class.java, mapOf("feature_name" to "Road Survey"))
        }

        var email : String = viewModel.getUserEmail().toString()

        viewModel.fetchProfile(email)

        viewModel.uiState.observe(viewLifecycleOwner) { state ->
            when (state) {
                is HomeUIState.Loading -> {
                }
                is HomeUIState.Success -> {
                    Glide.with(requireContext())
                        .load(state.result.profileImage_url)
                        .placeholder(R.drawable.ic_profile)
                        .error(R.drawable.ic_profile)
                        .into(profileImage)
                }

                is HomeUIState.Error -> {
                    Log.d("HomeFragments","Error in fetch Image")
                }

                HomeUIState.Idle -> TODO()
            }
        }

    }


    fun <T> openActivity(activityClass: Class<T>, extras: Map<String, String>? = null) {
        val intent = Intent(requireContext(), activityClass)

        extras?.forEach { (key, value) ->
            intent.putExtra(key, value)
        }
        startActivity(intent)
    }


    private val pickImageLauncher = registerForActivityResult(
        ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        uri?.let {
            val intent = Intent(requireContext(), FloodActivity::class.java)
            intent.putExtra("image_uri", it.toString())
            startActivity(intent)
        }
    }
}



//class ObjectDetectionActivity : AppCompatActivity() {
//
//    override fun onCreate(savedInstanceState: Bundle?) {
//        super.onCreate(savedInstanceState)
//        setContentView(R.layout.activity_object_detection)
//
//        val imageView = findViewById<ImageView>(R.id.previewImageView)
//
//        // Get URI from intent
//        val imageUriString = intent.getStringExtra("image_uri")
//        val imageUri = imageUriString?.let { Uri.parse(it) }
//
//        imageUri?.let {
//            imageView.setImageURI(it)  // Simple preview
//        }
//    }
//}
