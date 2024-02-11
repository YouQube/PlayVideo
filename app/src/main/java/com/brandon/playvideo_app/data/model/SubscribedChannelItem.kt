package com.brandon.playvideo_app.data.model

import java.time.LocalDateTime

data class SubscribedChannelItem(
    val channelId: String,
    val channelInfo: SubscribedChannelModel? = null
)

data class SubscribedChannelModel(
    val channelName: String? , // 채널 이름
    val videoList: List<SubscribedVideoModel>?, // 채널이 가진 영상 정보들
    val pageInfo: PageInfo?, // 페이지 정보
    val totalVideoCount: Int?, // 채널의 영상 총 개수
    val lastCountUpdatedTime: LocalDateTime? // 마지막 채널의 영상 총 개수 업데이트 시간
)

data class SubscribedVideoModel(
    val videoId: String?,   // 영상 id
    val title: String?,     // 영상 제목
    val description: String?,   // 영상 설명
    val thumbnails: YoutubeThumbnails?, // 영상 썸네일
    val channelTitle: String?,  // 채널 명
    val liveBroadcastContent: String?,  // 라이브 방송 여부
    val publishTime: LocalDateTime?    // 게시 시간
)