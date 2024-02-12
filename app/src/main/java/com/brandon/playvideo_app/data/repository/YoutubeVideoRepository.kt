package com.brandon.playvideo_app.data.repository

import com.brandon.playvideo_app.data.model.RepositoryResult
import com.brandon.playvideo_app.data.model.YoutubeVideoResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface YoutubeVideoRepository {

    @GET("videos")
    suspend fun fetchVideoInfoById(
        @Query("id") videoId: String,
        @Query("part") part: String? = "snippet,contentDetails,statistics"
    ): RepositoryResult<YoutubeVideoResponse>


    @GET("videos")
    suspend fun fetchVideoInfoByIds(
        @Query("id") videoIds: List<String>,
        @Query("part") part: String? = "snippet,contentDetails,statistics"
    ): RepositoryResult<YoutubeVideoResponse>
}