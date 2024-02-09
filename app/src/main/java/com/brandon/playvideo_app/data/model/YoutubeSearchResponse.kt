package com.brandon.playvideo_app.data.model

import com.google.gson.annotations.SerializedName

// YouTube 검색 응답에 대한 모델
data class YoutubeSearchResponse(
    @SerializedName("kind") // 데이터의 종류
    val kind: String?,
    @SerializedName("etag") // 엔터티 태그
    val etag: String?,
    @SerializedName("nextPageToken") // 다음 페이지 토큰
    val nextPageToken: String?,
    @SerializedName("regionCode") // 지역 코드
    val regionCode: String?,
    @SerializedName("pageInfo") // 페이지 정보
    val pageInfo: PageInfo?,
    @SerializedName("items") // 비디오 항목 목록
    val items: List<YoutubeSearchItem>?
)

// YouTube 비디오 검색 결과 항목에 대한 모델
data class YoutubeSearchItem(
    @SerializedName("kind") // 항목의 종류
    val kind: String?,
    @SerializedName("etag") // 엔터티 태그
    val etag: String?,
    @SerializedName("id") // 비디오 ID
    val id: YoutubeVideoId?,
    @SerializedName("snippet") // 비디오 스니펫 정보
    val snippet: YoutubeVideoSnippet?
)

// YouTube 비디오 ID에 대한 모델
data class YoutubeVideoId(
    @SerializedName("kind") // ID의 종류
    val kind: String?,
    @SerializedName("videoId") // 비디오 ID
    val videoId: String?
)

// YouTube 비디오 스니펫 정보에 대한 모델
data class YoutubeVideoSnippet(
    @SerializedName("publishedAt") // 게시일
    val publishedAt: String?,
    @SerializedName("channelId") // 채널 ID
    val channelId: String?,
    @SerializedName("title") // 제목
    val title: String?,
    @SerializedName("description") // 설명
    val description: String?,
    @SerializedName("thumbnails") // 썸네일 이미지 정보
    val thumbnails: YoutubeThumbnails?,
    @SerializedName("channelTitle") // 채널 제목
    val channelTitle: String?,
    @SerializedName("liveBroadcastContent") // 라이브 방송 내용
    val liveBroadcastContent: String?,
    @SerializedName("publishTime") // 게시 시간
    val publishTime: String?
)


// YouTube 비디오 썸네일 정보에 대한 모델
data class YoutubeThumbnails(
    @SerializedName("default") // 기본 썸네일
    val default: Thumbnail?,
    @SerializedName("medium") // 중간 크기 썸네일
    val medium: Thumbnail?,
    @SerializedName("high") // 높은 해상도 썸네일
    val high: Thumbnail?
)

// 썸네일 이미지에 대한 모델
data class Thumbnail(
    @SerializedName("url") // 이미지 URL
    val url: String?,
    @SerializedName("width") // 이미지 너비
    val width: Int?,
    @SerializedName("height") // 이미지 높이
    val height: Int?
)

