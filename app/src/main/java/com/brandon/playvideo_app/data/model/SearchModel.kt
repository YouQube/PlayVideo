package com.brandon.playvideo_app.data.model

import com.google.gson.annotations.SerializedName
import java.util.Date

data class SearchModel (
    @SerializedName("kind")
    var kind : String,
    @SerializedName("etag")
    var etag : String,
    @SerializedName("nextPageToken")
    var nextPageToken : String,
    @SerializedName("prevPageToken")
    var prevPageToken : String,
    @SerializedName("regionCode")
    var regionCode : String,
    @SerializedName("pageInfo")
    var pageInfo : SearchPageInfo,
    @SerializedName("items")
    var items : List<SearchItem>
)

data class SearchItem(
    @SerializedName("kind")
    var kind : String,
    @SerializedName("etag")
    var etag : String,
    @SerializedName("id")
    var id : SearchId,
    @SerializedName("snippet")
    var snippet : Snippet,
    @SerializedName("channelTitle")
    var channelTitle : String?,
    @SerializedName("liveBroadcastContent")
    var liveBroadcastContent : String
//    @SerializedName("publishTime")
//    var publishTime : Date
)

data class SearchId(
    @SerializedName("kind")
    val kind : String,
    @SerializedName("videoId")
    val videoId : String,
    @SerializedName("channelId")
    val channelId : String,
    @SerializedName("playlistId")
    val playlistId : String
)

data class thumbnails(
    @SerializedName("default")
    var default : Default ,
    @SerializedName("medium")
    var medium : Medium,
    @SerializedName("high")
    var high : High
)

data class SearchPageInfo(
    @SerializedName("totalResults")
    val totalResults : Int,
    @SerializedName("resultsPerPage")
    val resultsPerPage : Int
)
