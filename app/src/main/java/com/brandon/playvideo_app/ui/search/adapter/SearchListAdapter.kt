package com.brandon.playvideo_app.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.databinding.RvitemSearchListBinding
import com.brandon.playvideo_app.model.SearchListItem
import com.bumptech.glide.Glide

class SearchListAdapter(searchListItems : MutableList<SearchListItem>) : RecyclerView.Adapter<SearchListAdapter.SearchListHolder>() {
    var items = searchListItems

    inner class SearchListHolder (private val binding: RvitemSearchListBinding) : RecyclerView.ViewHolder(binding.root){

        val tvTitle = binding.tvSearchTitle
        val tvUploader = binding.tvSearchUploader
        var tvViews = binding.tvSearchView
        val tvPlayTime = binding.tvSearchPlayTime
        val ivThumbnail = binding.ivSearchThumbnail

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchListHolder {
        val binding = RvitemSearchListBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchListHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SearchListHolder, position: Int) {

        holder.tvTitle.text = items[position].title
        holder.tvUploader.text = items[position].uploader
        holder.tvViews.text = items[position].viewCount.toString()
        holder.tvPlayTime.text = items[position].playTime

        Glide.with(holder.itemView)
            .load(items[position].thumbnail)
            .into(holder.ivThumbnail)


    }

}