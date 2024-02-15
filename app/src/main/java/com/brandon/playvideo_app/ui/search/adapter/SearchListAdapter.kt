package com.brandon.playvideo_app.ui.search.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.databinding.ItemVideoLibraryBinding
import com.brandon.playvideo_app.databinding.RvitemSearchListBinding
import com.brandon.playvideo_app.model.SearchListItem
import com.brandon.playvideo_app.ui.detail.ChannelDetailActivity
import com.brandon.playvideo_app.util.Converter
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
        val tvPlayTime = binding.tvSearchAmount
        val ivThumbnail = binding.ivSearchThumbnail
        val clThumbnail = binding.clSearchItem
        val originViews = binding.originViews
        val tvChaneelId = binding.tvChannelId

        init {
            ivThumbnail.setOnClickListener(this)
//            clThumbnail.setOnClickListener {
//                val position = adapterPosition.takeIf { it != RecyclerView.NO_POSITION }
//                if(position != RecyclerView.NO_POSITION){
//                    val item = items[position!!]
//                    listener.onClicked(position)
//                }
//            }
            tvUploader.setOnClickListener {
                val intent = Intent(binding.root.context, ChannelDetailActivity::class.java)
                intent.putExtra("data",tvChaneelId.text.toString())
                binding.root.context.startActivity(intent)
            }
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
        holder.tvViews.text = Converter.formatViews(items[position].viewCount)
        holder.originViews.text = items[position].viewCount.toString()
        holder.tvPlayTime.text = items[position].videoCount
        holder.tvChaneelId.text = items[position].channelId

        Glide.with(holder.itemView)
            .load(items[position].thumbnail)
            .into(holder.ivThumbnail)


    }

}