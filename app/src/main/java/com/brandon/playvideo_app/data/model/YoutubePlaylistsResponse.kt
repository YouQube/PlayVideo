package com.brandon.playvideo_app.data.model

import com.google.gson.annotations.SerializedName

data class PlaylistsResponse (
    @SerializedName("etag")
    val etag: String?,
    @SerializedName("items")
    val items: List<PlaylistsItem>?,
    @SerializedName("kind")
    val kind: String?,
    @SerializedName("nextPageToken")
    val nextPageToken: String?,
    @SerializedName("pageInfo")
    val pageInfo: PageInfo?
)

data class PlaylistsItem(
    @SerializedName("kind") // 항목의 종류
    val kind: String?,
    @SerializedName("etag") // 엔터티 태그
    val etag: String?,
    @SerializedName("id")
    val id: String?,
    @SerializedName("snippet") // 비디오 스니펫 정보
    val snippet: PlaylistsSnippet?,
    val contentDetails : PlaylistsContentDetails
)

data class PlaylistsSnippet(
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
    val channelTitle: String?
)

data class PlaylistsContentDetails(
    val itemCount : Int
)