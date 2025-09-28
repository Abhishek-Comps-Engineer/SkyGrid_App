package com.example.abhishek.project.internship.fragments

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.abhishek.project.internship.adapters.HistoryAdapter
import com.example.abhishek.project.internship.databinding.FragmentHistoryBinding
import com.example.abhishek.project.internship.repositories.HistoryRepository
import com.example.abhishek.project.internship.viewmodels.AppViewModelFactory
import com.example.abhishek.project.internship.viewmodels.HistoryViewModel

class HistoryFragment : Fragment() {

    private var _binding: FragmentHistoryBinding? = null
    private val binding get() = _binding!!
    private val TAG = "HistoryFragment"
    private val repo = HistoryRepository()
    private lateinit var viewModel: HistoryViewModel
    private lateinit var adapter: HistoryAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentHistoryBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(
            this,
            AppViewModelFactory { HistoryViewModel(repo) }
        )[HistoryViewModel::class.java]

        adapter = HistoryAdapter(emptyList())
        binding.historyRecyclerView.layoutManager = LinearLayoutManager(requireContext())
        binding.historyRecyclerView.adapter = adapter

        viewModel.history.observe(viewLifecycleOwner) { list ->
            Log.d(TAG, "History data received: ${list.size} items")
            adapter.updateData(list)

            binding.emptyStateView.visibility = if (list.isEmpty()) View.VISIBLE else View.GONE
        }

        viewModel.isLoading.observe(viewLifecycleOwner) { isLoading ->
            Log.d(TAG, "Loading state: $isLoading")
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        }

        viewModel.error.observe(viewLifecycleOwner) { error ->
            if (error != null) {
                Log.e(TAG, "Error fetching history: $error")
            }
        }
        viewModel.loadHistory(page = 1, limit = 20)
        Log.d(TAG, "Loading history from page 1, limit 20")

    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
