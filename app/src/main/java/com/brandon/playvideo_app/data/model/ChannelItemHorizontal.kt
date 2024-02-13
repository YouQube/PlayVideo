package com.brandon.playvideo_app.data.model

import timber.log.Timber
import java.time.Duration
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter


// id, image, name, isActive
data class ChannelItemHorizontal(
    val channelId: String,
    val channelName: String?,
    val channelIconImageUrl: String?,
    val isActive: Boolean = false
)

fun SubscribedChannelModel.toChannelItem(): ChannelItemHorizontal {
    val channelName = this.channelInfo?.snippet?.title
    val channelIconImageUrl = this.channelInfo?.snippet?.thumbnails?.default?.url
    val isActive = isPublishedWithinDays(this.videos?.firstOrNull()?.info?.snippet?.publishedAt)
    val channelItemHorizontal = ChannelItemHorizontal(
        channelId = this.channelId,
        channelName = channelName,
        channelIconImageUrl = channelIconImageUrl,
        isActive = isActive
    )

    return channelItemHorizontal
}

fun isPublishedWithinDays(publishedAt: String?): Boolean {
    publishedAt ?: return false

    // 현재 시간을 가져옵니다.
    val currentTime = LocalDateTime.now()

    // publishedAt 문자열을 LocalDateTime 객체로 변환합니다.
    val formatter = DateTimeFormatter.ISO_DATE_TIME
    val publishedAtDateTime = LocalDateTime.parse(publishedAt, formatter)

    // 두 날짜 간의 차이를 계산합니다.
    val duration = Duration.between(publishedAtDateTime, currentTime)


    // 차이가 10일 이내인지 확인하고 결과를 반환합니다.
    return duration.toDays() <= 3
}