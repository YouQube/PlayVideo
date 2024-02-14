package com.brandon.playvideo_app.data.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

/**
 * 채널id
 * 채널 이름
 * 채널 아이콘 이미지
 * 채널 구독 자 수
 *
 * 영상 id
 * 영상 제목
 * 영상 조회수
 * 영상 업로드 시간
 * 영상 설명
 * 영상 요약 = null
 */

@Parcelize
data class VideoEntity(
    val channelId: String?,
    val channelTitle: String?,
    val channelIconImage: String?,
    val channelSubscriberCount: String?,
    val videoId: String?,
    val videoTitle: String?,
    val videoViewCount: Int?,
    val videoCommentCount: Int?,
    val videoPublishedAt: String?,
    val videoThumbnail: String?,
    val videoDescription: String?,
    val videoSummary: String? = null
): Parcelable
