package com.brandon.playvideo_app.data.category

import com.google.gson.annotations.SerializedName

data class CategoryVideoModel(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("items")
    val items: List<CategoryItem>
)

data class CategoryItem(
    @SerializedName("kind")
    val kind: String,
    @SerializedName("etag")
    val etag: String,
    @SerializedName("id")
    val id: String,
    @SerializedName("snippet")
    val snippet: CategorySnippet,
)

data class CategorySnippet(
    @SerializedName("title")
    val title: String,
    @SerializedName("assignable")
    val assignable: Boolean,
    @SerializedName("channelId")
    val channelId: String,
)