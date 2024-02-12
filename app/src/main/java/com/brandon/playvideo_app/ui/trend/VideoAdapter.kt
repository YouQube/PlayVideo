package com.brandon.playvideo_app.ui.trend

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.data.model.Item
import com.brandon.playvideo_app.databinding.ItemTrendBinding
import com.bumptech.glide.Glide

class VideoAdapter :
    ListAdapter<Item, VideoAdapter.VideoHolder>(object : DiffUtil.ItemCallback<Item>() {
        override fun areItemsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem.id == newItem.id
        }

        override fun areContentsTheSame(oldItem: Item, newItem: Item): Boolean {
            return oldItem == newItem
        }
    }) {
    inner class VideoHolder(private val binding: ItemTrendBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            with(binding) {
                tvVideoTitle.text = item.snippet.title
                tvChannelTitle.text = item.snippet.channelTitle
                Glide.with(itemView).load(
                    item.snippet.thumbnails.maxres?.url ?: item.snippet.thumbnails.default.url
                ).into(ivThumbnail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemTrendBinding.inflate(inflater, parent, false)
        return VideoHolder(binding)
    }

    override fun getItemCount() = currentList.size

    override fun onBindViewHolder(holder: VideoHolder, position: Int) {
        holder.bind(currentList[position])
    }
}