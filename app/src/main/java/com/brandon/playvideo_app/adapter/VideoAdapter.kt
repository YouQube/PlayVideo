package com.brandon.playvideo_app.adapter

import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.databinding.ItemTrendBinding

class VideoAdapter:RecyclerView.Adapter<VideoAdapter.VideoHolder>() {
    inner class VideoHolder(private val binding:ItemTrendBinding):RecyclerView.ViewHolder(binding.root){
        fun bind(item:ItemTrendBinding){

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        TODO("Not yet implemented")
    }

    override fun getItemCount(): Int {
        TODO("Not yet implemented")
    }

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        TODO("Not yet implemented")
    }
}