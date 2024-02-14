package com.brandon.playvideo_app.ui.subscription.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.data.model.ChannelItemHorizontal
import com.brandon.playvideo_app.databinding.ItemSubscriptionHorizontalBinding
import com.bumptech.glide.Glide

class HorizontalChannelListAdapter() :
    ListAdapter<ChannelItemHorizontal, HorizontalChannelListAdapter.SubscribedChannelHolder>(ChannelItemHorizontalDiffCallback()) {

    inner class SubscribedChannelHolder(private val binding: ItemSubscriptionHorizontalBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: ChannelItemHorizontal) {
            with(binding){
                Glide.with(itemView.context)
                    .load(item.channelIconImageUrl)
                    .into(ivChannelIcon)
                tvChannelName.text = item.channelName
                viewIsActive.isVisible = item.isActive
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SubscribedChannelHolder {
        val binding = ItemSubscriptionHorizontalBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return SubscribedChannelHolder(binding)
    }

    override fun onBindViewHolder(holder: SubscribedChannelHolder, position: Int) {
        holder.bind(getItem(position))
    }


}

class ChannelItemHorizontalDiffCallback : DiffUtil.ItemCallback<ChannelItemHorizontal>() {
    override fun areItemsTheSame(oldItem: ChannelItemHorizontal, newItem: ChannelItemHorizontal): Boolean {
        // 아이템의 고유 식별자를 비교하여 동일한 아이템인지 확인합니다.
        return oldItem.channelId == newItem.channelId
    }

    override fun areContentsTheSame(oldItem: ChannelItemHorizontal, newItem: ChannelItemHorizontal): Boolean {
        // 아이템의 내용이 동일한지 확인합니다.
        // 여기에서는 전체 내용을 비교하지만, 성능상 필요한 경우 필드를 선택적으로 비교할 수 있습니다.
        return oldItem == newItem
    }
}