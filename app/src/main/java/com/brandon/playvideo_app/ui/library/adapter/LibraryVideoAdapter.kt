package com.brandon.playvideo_app.ui.library.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.data.local.entities.VideoEntity
import com.brandon.playvideo_app.databinding.ItemVideoLibraryBinding
import com.brandon.playvideo_app.util.Converter
import com.bumptech.glide.Glide


class LibraryVideoAdapter : ListAdapter<VideoEntity, LibraryVideoAdapter.VideosViewHolder>(diffCallback) {

    // 표시될 항목들
//    var items = mutableListOf<VideoEntity>()

    private lateinit var listener: OnItemClickListener

    inner class VideosViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemVideoLibraryBinding.bind(view)

        fun bind(video: VideoEntity) {
            with(binding) {
                tvVideoTitle.text = video.title
                tvVideoUploader.text = video.channelTitle
                tvVideoView.text = Converter.formatViews(video.views)

                Glide.with(itemView)
                    .load(video.url)
                    .into(ivVideoThumbnail)

                clVideoThumbItem.setOnClickListener {
                    listener.onClicked(video.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideosViewHolder {
        val binding = ItemVideoLibraryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return VideosViewHolder(binding.root)
    }


    override fun onBindViewHolder(holder: VideosViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

    interface OnItemClickListener {
        fun onClicked(videosId: Int)
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<VideoEntity>() {
            override fun areItemsTheSame(oldItem: VideoEntity, newItem: VideoEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: VideoEntity, newItem: VideoEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}