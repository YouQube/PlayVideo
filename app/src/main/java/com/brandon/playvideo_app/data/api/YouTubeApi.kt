package com.brandon.playvideo_app.data.api

import com.brandon.playvideo_app.data.model.CategoryVideoModel
import com.brandon.playvideo_app.data.model.TrendVideoModel
import retrofit2.http.GET
import retrofit2.http.Query

private const val PART = "snippet"
private const val CHART = "mostPopular"
private const val MAX_RESULT = 20
private const val REGION = "KR"
private const val API_KEY = "AIzaSyBK9jGXGSG50AwWRFEc35EoMvRFu0bPkWQ"
private const val HL = "ko_KR" //hl 매개변수는 API 응답의 텍스트 값에 사용할 언어를 지정합니다. 기본값은 en_US입니다.
private const val VIDEO_CATEGORY_ID = "0" //videoCategoryId 매개변수는 차트를 검색해야 하는 동영상 카테고리를 식별합니다. 이 매개변수는 chart 매개변수와만 함께 사용할 수 있습니다. 기본적으로 차트는 특정 카테고리로 제한되지 않습니다. 기본값은 0입니다.
interface YouTubeApi {
    @GET("videos")
    suspend fun getTrendingVideos(
        @Query("part") part: String = PART,
        @Query("chart") chart: String = CHART,
        @Query("maxResults") maxResults: Int = MAX_RESULT,
        @Query("regionCode") regionCode: String = REGION,
        @Query("videoCategoryId") videoCategoryId: String = VIDEO_CATEGORY_ID,
        @Query("key") apiKey: String = API_KEY
    ): TrendVideoModel

    @GET("videoCategories")
    suspend fun getCategoryIds(
        @Query("part") part: String = PART,
        @Query("regionCode") regionCode: String = REGION,
        @Query("hl") hl:String = HL,
        @Query("key") apiKey: String = API_KEY
    ): CategoryVideoModel
}