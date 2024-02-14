package com.brandon.playvideo_app.ui.detail.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isGone
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.databinding.RvitemSearchListBinding
import com.brandon.playvideo_app.model.SearchListItem
import com.brandon.playvideo_app.ui.detail.ChannelDetailActivity
import com.brandon.playvideo_app.ui.search.adapter.SearchListAdapter
import com.bumptech.glide.Glide

class ChannelDetailPlallistsAdapter(searchListItems : MutableList<SearchListItem>) : RecyclerView.Adapter<ChannelDetailPlallistsAdapter.PlaylistsHolder>() {
    var items = searchListItems

    inner class PlaylistsHolder (private val binding: RvitemSearchListBinding) : RecyclerView.ViewHolder(binding.root){

        val tvTitle = binding.tvSearchTitle
        val tvUploader = binding.tvSearchUploader
        var tvViews = binding.tvSearchView
        val tvvideoCount = binding.tvSearchAmount
        val ivThumbnail = binding.ivSearchThumbnail

        init {
            tvUploader.setOnClickListener {
                val intent = Intent(binding.root.context, ChannelDetailActivity::class.java)
                binding.root.context.startActivity(intent)
            }
        }

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlaylistsHolder {
        val binding = RvitemSearchListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return PlaylistsHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: PlaylistsHolder, position: Int) {

        holder.tvTitle.text = items[position].title
        holder.tvUploader.text = items[position].uploader
        holder.tvViews.isGone = true
        holder.tvvideoCount.isGone = false
        holder.tvvideoCount.text = items[position].videoCount.toString()


        Glide.with(holder.itemView)
            .load(items[position].thumbnail)
            .into(holder.ivThumbnail)
    }

}