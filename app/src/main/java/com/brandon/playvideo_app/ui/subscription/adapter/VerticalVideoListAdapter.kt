package com.brandon.playvideo_app.ui.subscription.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.data.model.VideoItemVertical
import com.brandon.playvideo_app.databinding.ItemSubscriptionVerticalBinding
import com.brandon.playvideo_app.util.Converter.formatPublishedTime
import com.brandon.playvideo_app.util.Converter.formatViews
import com.bumptech.glide.Glide

class VerticalVideoListAdapter(
    private val onItemClick: (String) -> Unit
) :
    ListAdapter<VideoItemVertical, VerticalVideoListAdapter.SubscribedChannelVideoHolder>(
        VideoItemVerticalDiffCallback()
    ) {

    inner class SubscribedChannelVideoHolder(
        private val binding: ItemSubscriptionVerticalBinding,
        private val onItemClick: (String) -> Unit
    ) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: VideoItemVertical) {
            with(binding) {
                Glide.with(itemView.context)
                    .load(item.videoThumbnailUrl)
                    .into(ivThumbnail)
                Glide.with(itemView.context)
                    .load(item.channelIconImageUrl)
                    .into(ivChannelIcon)
                tvTitle.text = item.videoTitle
                val detail =
                    "${item.channelName} · 조회수 ${formatViews(item.viewers)} · ${formatPublishedTime(item.publishedAt)}"
                tvDetail.text = detail

                container.setOnClickListener {
                    onItemClick(item.videoId)
                }
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribedChannelVideoHolder {
        val binding =
            ItemSubscriptionVerticalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubscribedChannelVideoHolder(binding, onItemClick)
    }

    override fun onBindViewHolder(holder: SubscribedChannelVideoHolder, position: Int) {
        holder.bind(getItem(position))
    }
}

class VideoItemVerticalDiffCallback : DiffUtil.ItemCallback<VideoItemVertical>() {
    override fun areItemsTheSame(oldItem: VideoItemVertical, newItem: VideoItemVertical): Boolean {
        // 아이템의 고유 식별자를 비교하여 동일한 아이템인지 확인합니다.
        return oldItem.videoId == newItem.videoId
    }

    override fun areContentsTheSame(oldItem: VideoItemVertical, newItem: VideoItemVertical): Boolean {
        // 아이템의 내용이 동일한지 확인합니다.
        // 여기에서는 전체 내용을 비교하지만, 성능상 필요한 경우 필드를 선택적으로 비교할 수 있습니다.
        return oldItem == newItem
    }
}