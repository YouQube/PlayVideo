package com.brandon.playvideo_app.ui.search.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.databinding.RvitemSearchShortsBinding
import com.brandon.playvideo_app.model.SearchListItem
import com.bumptech.glide.Glide

class SearchShortsAdapter(searchListItems : MutableList<SearchListItem>) : RecyclerView.Adapter<SearchShortsAdapter.SearchShortsHolder>() {
    var items = searchListItems

    inner class SearchShortsHolder (private val binding: RvitemSearchShortsBinding) : RecyclerView.ViewHolder(binding.root){

        val tvTitle = binding.tvSearchTitle
        val tvUploader = binding.tvSearchUploader
        var tvViews = binding.tvSearchView
        val ivThumbnail = binding.ivSearchThumbnail

    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SearchShortsHolder {
        val binding = RvitemSearchShortsBinding.inflate(LayoutInflater.from(parent.context),parent,false)
        return SearchShortsHolder(binding)
    }

    override fun getItemCount(): Int {
        return items.size
    }

    override fun onBindViewHolder(holder: SearchShortsHolder, position: Int) {

        holder.tvTitle.text = items[position].title
        holder.tvUploader.text = items[position].uploader
        holder.tvViews.text = items[position].viewCount.toString()

        Glide.with(holder.itemView)
            .load(items[position].thumbnail)
            .into(holder.ivThumbnail)


    }

}