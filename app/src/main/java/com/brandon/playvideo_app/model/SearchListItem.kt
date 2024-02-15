package com.brandon.playvideo_app.model

class SearchListItem(
    var title : String?,
    val uploader : String?,
    var viewCount : Int?,
    var thumbnail : String?,
    var description : String?,
    var isInterset : Boolean,
    var videoCount : String?,
    var playTime : Long?,
    val channelId : String?
)