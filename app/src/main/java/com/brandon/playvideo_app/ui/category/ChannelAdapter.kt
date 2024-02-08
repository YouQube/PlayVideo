package com.brandon.playvideo_app.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.data.model.ChannelItem
import com.brandon.playvideo_app.databinding.ItemChannelByCategoryBinding
import com.bumptech.glide.Glide

class ChannelAdapter :
    RecyclerView.Adapter<ChannelAdapter.ChannelViewHolder>() {

    var channelItem: List<ChannelItem> = listOf()

    inner class ChannelViewHolder(private val binding: ItemChannelByCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChannelItem) {
            with(binding) {
                tvChannelName.text = item.snippet.title
                Glide.with(itemView)
                    .load(item.snippet.thumbnails.high?.url ?: item.snippet.thumbnails.default.url).into(ivChannelThumbnail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ChannelViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemChannelByCategoryBinding.inflate(inflater, parent, false)
        return ChannelViewHolder(binding)
    }

    override fun getItemCount() = channelItem.size

    override fun onBindViewHolder(holder: ChannelViewHolder, position: Int) {
        holder.bind(channelItem[position])
    }
}