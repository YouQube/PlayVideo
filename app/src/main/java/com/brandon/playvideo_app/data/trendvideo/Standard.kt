package com.brandon.playvideo_app.data.trendvideo

import com.google.gson.annotations.SerializedName

data class Standard(
    @SerializedName("height")
    val height: Int,
    @SerializedName("url")
    val url: String,
    @SerializedName("width")
    val width: Int
)