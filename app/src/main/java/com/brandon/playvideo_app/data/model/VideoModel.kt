package com.brandon.playvideo_app.data.model

import com.google.gson.annotations.SerializedName

data class VideoModel (
    @SerializedName("kind")
    var kind : String,
    @SerializedName("etag")
    var etag : String,
    @SerializedName("pageInfo")
    var pageInfo : VideoPageInfo,
    @SerializedName("items")
    var item : ArrayList<VideoItem>
)

data class VideoPageInfo (
    @SerializedName("totalResults")
    val totalResults : Int,
    @SerializedName("resultsPerPage")
    val resultsPerPage : Int
)

data class VideoItem(
    @SerializedName("kind")
    var kind : String,
    @SerializedName("etag")
    var etag : String,
    @SerializedName("id")
    var id : String,
    @SerializedName("statistics")
    var statistics : Statistics
)

data class Statistics(
    @SerializedName("viewCount")
    val viewCount: Int,
    @SerializedName("likeCount")
    val likeCount : Int,
    @SerializedName("favoriteCount")
    val favoriteCount : Int,
    @SerializedName("commentCount")
    val commentCount : Int
)

