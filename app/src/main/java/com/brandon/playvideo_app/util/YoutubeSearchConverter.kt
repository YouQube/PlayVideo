package com.brandon.playvideo_app.util

import com.brandon.playvideo_app.data.model.SubscribedChannelModel
import com.brandon.playvideo_app.data.model.SubscribedVideoModel
import com.brandon.playvideo_app.data.model.VideoModel
import com.brandon.playvideo_app.data.model.YoutubeSearchItem
import com.brandon.playvideo_app.data.model.YoutubeSearchResponse
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object YoutubeSearchConverter {

    fun convertToSubscribedChannelModel(youtubeSearchResponse: YoutubeSearchResponse?): SubscribedChannelModel? {
        youtubeSearchResponse?.let { response ->
            val videoList = response.items?.mapNotNull { convertToVideoModel(it) }
            return SubscribedChannelModel(
                channelName = videoList?.firstOrNull()?.channelTitle,
                videoList = videoList,
                totalVideoCount = null, // 나중에 다른 api call 로 데이터 초기화 해야 함
                lastCountUpdatedTime = LocalDateTime.now(),
                pageInfo = response.pageInfo
            )
        }
        return null
    }

    private fun convertToVideoModel(youtubeSearchItem: YoutubeSearchItem): SubscribedVideoModel? {
        val snippet = youtubeSearchItem.snippet ?: return null
        val thumbnails = snippet.thumbnails
        val publishTime = snippet.publishTime?.let { convertPublishTimeToLocalDateTime(it) }
        return SubscribedVideoModel(
            videoId = youtubeSearchItem.id?.videoId,
            title = snippet.title,
            description = snippet.description,
            thumbnails = thumbnails,
            channelTitle = snippet.channelTitle,
            liveBroadcastContent = snippet.liveBroadcastContent,
            publishTime = publishTime
        )
    }

    // 함수 호출 전 입력값에 대해 세이프콜을 적용함으로 non-nullable 한 함수로 작성
    private fun convertPublishTimeToLocalDateTime(publishTime: String): LocalDateTime? {
        return try {
            val instant = Instant.parse(publishTime)
            LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        } catch (e: Exception) {
            null
        }
    }
}
