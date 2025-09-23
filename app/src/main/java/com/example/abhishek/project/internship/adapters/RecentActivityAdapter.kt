package com.example.abhishek.project.internship.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import com.example.abhishek.project.internship.R
import com.example.abhishek.project.internship.model.RecentActivity


class RecentActivityAdapter(
    private var items: List<RecentActivity>
) : RecyclerView.Adapter<RecentActivityAdapter.ActivityViewHolder>() {

    class ActivityViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val icon: ImageView = itemView.findViewById(R.id.activity_icon)
        val title: TextView = itemView.findViewById(R.id.activity_title)
        val date: TextView = itemView.findViewById(R.id.activity_date)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ActivityViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_recent_activity, parent, false)
        return ActivityViewHolder(view)
    }

    override fun onBindViewHolder(holder: ActivityViewHolder, position: Int) {
        val item = items[position]
        holder.title.text = item.title
        holder.date.text = item.date
        holder.icon.setImageResource(item.iconRes)
        holder.icon.setColorFilter(ContextCompat.getColor(holder.itemView.context, item.tintColor))
    }

    override fun getItemCount() = items.size

    fun updateList(newItems: List<RecentActivity>) {
        items = newItems
        notifyDataSetChanged()
    }
}
