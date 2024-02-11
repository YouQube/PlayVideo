package com.brandon.playvideo_app.data.repository

import com.brandon.playvideo_app.data.model.RepositoryResult
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeChannelRepository {
    @GET("channels")
    suspend fun fetchChannelInfoById(
        @Query("id") channelId: String,
        @Query("part") part: String? = "snippet%2Cstatistics",
    )
}