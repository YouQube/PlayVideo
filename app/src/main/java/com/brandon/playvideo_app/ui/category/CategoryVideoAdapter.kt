package com.brandon.playvideo_app.ui.category

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.data.model.Item
import com.brandon.playvideo_app.databinding.ItemCategoryBinding
import com.bumptech.glide.Glide

class CategoryVideoAdapter(var items: List<Item>) :
    RecyclerView.Adapter<CategoryVideoAdapter.CategoryViewHolder>() {
    inner class CategoryViewHolder(private val binding: ItemCategoryBinding) :
        RecyclerView.ViewHolder(binding.root) {
        fun bind(item: Item) {
            with(binding) {
                tvCategoryTitle.text = item.snippet.title
                Glide.with(itemView).load(item.snippet.thumbnails.maxres?.url ?: item.snippet.thumbnails.default)
                    .into(ivCategoryThumbnail)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        val inflater =
            parent.context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val binding = ItemCategoryBinding.inflate(inflater, parent, false)
        return CategoryViewHolder(binding)
    }

    override fun getItemCount() = items.size

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.bind(items[position])
    }
}