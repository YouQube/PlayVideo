package com.brandon.playvideo_app.data.model

data class SubscribedChannelModel(
    val channelId: String,
    var channelInfo: ChannelInfoModel? = null,
    var videos: List<SubscribedVideoModel>? = null,
    var nextPageToken: String? = null,
)

data class ChannelInfoModel(val channelId: String, val snippet: ChannelSnippet?, val statistics: ChannelStatistics?)

data class SubscribedVideoModel(val videoId: String, var info: VideoInfoModel?)

data class VideoInfoModel(val videoId: String, val snippet:VideoSnippet?, val statistics: VideoStatistics?, val contentDetails: VideoContentDetails?)
