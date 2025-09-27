package com.example.abhishek.project.internship.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.abhishek.project.internship.R
import com.example.abhishek.project.internship.adapters.RecentActivityAdapter
import com.example.abhishek.project.internship.ui.FloodActivity
import com.example.abhishek.project.internship.ui.LandAnalysisActivity
import com.example.abhishek.project.internship.ui.ObjectDetectionActivity
import com.example.abhishek.project.internship.ui.PotholeActivity
import com.example.abhishek.project.internship.viewmodels.HomeViewModel
import com.google.android.material.card.MaterialCardView


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
        val recyclerView = view.findViewById<RecyclerView>(R.id.recycler_recent)
        val cardObjectDetection = view.findViewById<MaterialCardView>(R.id.card_object_detection)
        val cardLandAnalysis = view.findViewById<MaterialCardView>(R.id.card_land_analysis)
        val cardFloodDetection = view.findViewById<MaterialCardView>(R.id.card_flood_detection)
        val cardRoadSurvey = view.findViewById<MaterialCardView>(R.id.card_road_survey)

        adapter = RecentActivityAdapter(emptyList())
        recyclerView.layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        recyclerView.adapter = adapter

        // Observe data
        viewModel.userName.observe(viewLifecycleOwner) { userName ->
            userNameText.text = userName
        }

        viewModel.recentActivities.observe(viewLifecycleOwner) { activities ->
            adapter.updateList(activities)
        }

        cardObjectDetection.setOnClickListener {
            openActivity(ObjectDetectionActivity::class.java, mapOf("feature_name" to "Object Detection"))
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


    }

    fun <T> openActivity(activityClass: Class<T>, extras: Map<String, String>? = null) {
        val intent = Intent(requireContext(), activityClass)

        extras?.forEach { (key, value) ->
            intent.putExtra(key, value)
        }
        startActivity(intent)
    }

}
