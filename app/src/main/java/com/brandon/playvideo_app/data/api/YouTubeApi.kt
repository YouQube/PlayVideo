package com.brandon.playvideo_app.data.api

import com.brandon.playvideo_app.data.category.CategoryVideoModel
import com.brandon.playvideo_app.data.trendvideo.TrendVideoModel
import retrofit2.http.GET
import retrofit2.http.Query

private const val PART = "snippet"
private const val CHART = "mostPopular"
private const val MAX_RESULT = 20
private const val REGION = "KR"
private const val API_KEY = "AIzaSyBK9jGXGSG50AwWRFEc35EoMvRFu0bPkWQ"

interface YouTubeApi {
    @GET("videos")
    suspend fun getTrendingVideos(
        @Query("part") part: String = PART,
        @Query("chart") chart: String = CHART,
        @Query("maxResults") maxResults: Int = MAX_RESULT,
        @Query("regionCode") regionCode: String = REGION,
        @Query("videoCategoryId") videoCategoryId: String? = null,
        @Query("key") apiKey: String = API_KEY
    ): TrendVideoModel

    @GET("videoCategories")
    suspend fun getCategoriesId(
        @Query("part") part: String = PART,
        @Query("regionCode") regionCode: String = REGION,
        @Query("id") id: String,
    ): CategoryVideoModel
}