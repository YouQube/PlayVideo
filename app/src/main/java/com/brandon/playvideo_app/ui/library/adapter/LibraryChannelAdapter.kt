package com.brandon.playvideo_app.ui.library.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.data.local.entities.ChannelEntity
import com.brandon.playvideo_app.databinding.ItemChannelLibraryBinding
import com.bumptech.glide.Glide


class LibraryChannelAdapter : ListAdapter<ChannelEntity, LibraryChannelAdapter.ChannelsViewHolder>(diffCallback) {

    // 표시될 항목들
//    var items = mutableListOf<ChannelEntity>()

    private lateinit var listener: OnItemClickListener

    inner class ChannelsViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        private val binding = ItemChannelLibraryBinding.bind(view)

        fun bind(channel: ChannelEntity) {
            with(binding) {
                tvChannelTitle.text = channel.channelTitle

                Glide.with(itemView)
                    .load(channel.url)
                    .into(ivChannelThumbnail)

                clChannelThumbItem.setOnClickListener {
                    listener.onClicked(channel.id)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelsViewHolder {
        val binding = ItemChannelLibraryBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ChannelsViewHolder(binding.root)
    }


    override fun onBindViewHolder(holder: ChannelsViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    fun setOnClickListener(listener1: OnItemClickListener) {
        listener = listener1
    }

    interface OnItemClickListener {
        fun onClicked(videosId: Int)
    }

    companion object {
        val diffCallback = object : DiffUtil.ItemCallback<ChannelEntity>() {
            override fun areItemsTheSame(oldItem: ChannelEntity, newItem: ChannelEntity): Boolean {
                return oldItem.id == newItem.id
            }

            override fun areContentsTheSame(oldItem: ChannelEntity, newItem: ChannelEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}