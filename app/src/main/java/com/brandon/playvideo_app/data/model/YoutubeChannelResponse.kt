package com.brandon.playvideo_app.data.model

import com.google.gson.annotations.SerializedName

data class YoutubeChannelResponse(
    val etag: String,
    @SerializedName("items")
    val items: List<ChannelItem>,
    val kind: String,
    @SerializedName("pageInfo")
    val pageInfo: ChannelPageInfo
)

data class ChannelThumbnails(
    @SerializedName("default")
    val default: ChannelDefault,
    @SerializedName("high")
    val high: ChannelHigh?,
    @SerializedName("medium")
    val medium: ChannelMedium?
)

data class ChannelSnippet(
    val description: String,
    val localized: ChannelLocalized,
    val publishedAt: String,
    @SerializedName("thumbnails")
    val thumbnails: ChannelThumbnails,
    @SerializedName("title")
    val title: String,
    val customUrl : String
)

data class ChannelPageInfo(
    @SerializedName("resultsPerPage")
    val resultsPerPage: Int,
    @SerializedName("totalResults")
    val totalResults: Int
)

data class ChannelMedium(
    @SerializedName("height")
    val height: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)

data class ChannelItem(
    val etag: String,
    @SerializedName("id")
    val id: String,
    val kind: String,
    @SerializedName("snippet")
    val snippet: ChannelSnippet,
    val statistics: ChannelStatistics? = null,
    val brandingSettings: ChannelBrandingSettings? = null
)

data class ChannelHigh(
    @SerializedName("height")
    val height: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)

data class ChannelDefault(
    @SerializedName("height")
    val height: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)

data class ChannelLocalized(
    @SerializedName("description")
    val description: String,
    @SerializedName("title")
    val title: String
)

data class ChannelStatistics(
    val viewCount: String? = null,
    val subscriberCount: String? = null,
    val hiddenSubscriberCount: Boolean? = null,
    val videoCount: Int? = null,
)

data class ChannelBrandingSettings(
    val channel : ChannelChannel?,
    val image : ChannelImage?
)

data class ChannelChannel(
    val title : String?,
    val description: String?,
    val keywords : String?,
    val defaultLanguage : String?,
    val country : String?
)

data class ChannelImage(
    val bannerExternalUrl : String?
)
