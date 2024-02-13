package com.brandon.playvideo_app.data.model

data class VideoItemVertical(
    val videoId: String,
    val videoTitle: String?,
    val channelName: String?,
    val viewers: Int?,
    val publishedAt: String?,
    val videoThumbnailUrl: String?,
    val channelIconImageUrl: String?,
)

fun SubscribedChannelModel.toVideoItem(): List<VideoItemVertical>? {
    val videos = this.videos
    return videos?.map { video ->
        VideoItemVertical(
            videoId = video.videoId,
            videoTitle = video.info?.snippet?.title,
            channelName = video.info?.snippet?.channelTitle,
            viewers = video.info?.statistics?.viewCount,
            publishedAt = video.info?.snippet?.publishedAt,
            videoThumbnailUrl = video.info?.snippet?.thumbnails?.maxres?.url,
            channelIconImageUrl = this.channelInfo?.snippet?.thumbnails?.default?.url,
        )
    }
}