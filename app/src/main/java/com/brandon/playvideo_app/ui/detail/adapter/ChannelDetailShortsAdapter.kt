package com.brandon.playvideo_app.ui.detail.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.databinding.RvitemSearchListBinding
import com.brandon.playvideo_app.databinding.RvitemSearchShortsBinding
import com.brandon.playvideo_app.model.SearchListItem
import com.brandon.playvideo_app.ui.detail.ChannelDetailActivity
import com.brandon.playvideo_app.ui.search.adapter.SearchListAdapter
import com.bumptech.glide.Glide

class ChannelDetailShortsAdapter(searchListItems : MutableList<SearchListItem>) : RecyclerView.Adapter<ChannelDetailShortsAdapter.PlayShortsHolder>() {
    var items = searchListItems

    inner class PlayShortsHolder (private val binding: RvitemSearchShortsBinding) : RecyclerView.ViewHolder(binding.root){
        val tvTitel = binding.tvSearchTitle
        val tvUploader = binding.tvSearchUploader
        var tvViews = binding.tvSearchView
        val ivThumbnail = binding.ivSearchThumbnail

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayShortsHolder {
        val binding = RvitemSearchShortsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PlayShortsHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PlayShortsHolder, position: Int) {
        holder.tvTitel.isGone = true
        holder.tvUploader.isGone = true
        holder.tvViews.isGone = true
        holder.tvViews.text = items[position].viewCount.toString()

        Glide.with(holder.itemView)
            .load(items[position].thumbnail)
            .into(holder.ivThumbnail)
    }

}