package com.brandon.playvideo_app.ui.search.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.databinding.ItemVideoLibraryBinding
import com.brandon.playvideo_app.databinding.RvitemSearchListBinding
import com.brandon.playvideo_app.model.SearchListItem
import com.bumptech.glide.Glide



class SearchListAdapter(searchListItems : MutableList<SearchListItem>) : RecyclerView.Adapter<SearchListAdapter.SearchListHolder>() {
    var items = searchListItems

    private var listener: OnItemClickListener? = null

    interface OnItemClickListener {
        fun onClicked(item: SearchListItem)
    }

    inner class SearchListHolder (private val binding: RvitemSearchListBinding) : RecyclerView.ViewHolder(binding.root), View.OnClickListener{

        val tvTitle = binding.tvSearchTitle
        val tvUploader = binding.tvSearchUploader
        var tvViews = binding.tvSearchView
        val tvPlayTime = binding.tvSearchPlayTime
        val ivThumbnail = binding.ivSearchThumbnail
        val clThumbnail = binding.clSearchItem

        init {
            ivThumbnail.setOnClickListener(this)
//            clThumbnail.setOnClickListener {
//                val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION }
//                if(position != RecyclerView.NO_POSITION){
//                    val item = items[position!!]
//                    listener.onClicked(position)
//                }
//            }
        }

        // 아이템 클릭 이벤트 처리
        override fun onClick(view: View) {
            val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION } ?: return
            val item = items[position]

            listener?.onClicked(item)

            // 아이템 클릭 후 Like 값에 따른 이미지 visibility 갱신
            notifyItemChanged(position)
        }


    }

    fun setOnClickListener(listener1: OnItemClickListener) {
        listener = listener1
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