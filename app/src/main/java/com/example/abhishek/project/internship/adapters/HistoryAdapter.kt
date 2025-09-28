package com.example.abhishek.project.internship.adapters

import android.util.Log
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.abhishek.project.internship.databinding.ListItemHistoryBinding
import com.example.abhishek.project.internship.model.History
import java.text.SimpleDateFormat
import java.util.Locale

class HistoryAdapter(private var historyList: List<History>) :
    RecyclerView.Adapter<HistoryAdapter.HistoryViewHolder>() {

    inner class HistoryViewHolder(val binding: ListItemHistoryBinding) :
        RecyclerView.ViewHolder(binding.root)

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): HistoryViewHolder {
        val binding = ListItemHistoryBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return HistoryViewHolder(binding)
    }

    override fun getItemCount(): Int = historyList.size

    override fun onBindViewHolder(holder: HistoryViewHolder, position: Int) {
        val item = historyList[position]
        Log.d("HistoryAdapter", "Binding item: ${item.activityType} at ${item.createdAt}")

        holder.binding.historyItemTitle.text = item.activityType
        holder.binding.historyItemStatusChip.text = "Conditions: " +item.activityDetail
        holder.binding.historyItemTimestamp.text =
            SimpleDateFormat("MMM dd, yyyy 'Time:' hh:mm a", Locale.getDefault())
                .format(item.createdAt)


        Log.d("Path", item.imageUrl ?: "No image URL")
        Glide.with(holder.binding.root.context)
            .load(item.imageUrl)
            .override(80, 80)
            .centerCrop()
            .into(holder.binding.historyItemImage)
    }

    fun updateData(newList: List<History>) {
        historyList = newList
        notifyDataSetChanged()
    }
}
