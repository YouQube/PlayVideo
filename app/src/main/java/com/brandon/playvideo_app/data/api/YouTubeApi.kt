package com.brandon.playvideo_app.data.api

import com.brandon.playvideo_app.data.model.CategoryVideoModel
import com.brandon.playvideo_app.data.model.ChannelByCategoryModel
import com.brandon.playvideo_app.data.model.TrendVideoModel
import com.brandon.playvideo_app.data.model.YoutubeSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val PART = "snippet"
private const val CHART = "mostPopular"
private const val MAX_RESULT = 20
private const val REGION = "KR"
private const val API_KEY = "AIzaSyBK9jGXGSG50AwWRFEc35EoMvRFu0bPkWQ"
private const val HL = "ko_KR" //hl 매개변수는 API 응답의 텍스트 값에 사용할 언어를 지정합니다. 기본값은 en_US입니다.
private const val VIDEO_CATEGORY_ID =
    "0" //videoCategoryId 매개변수는 차트를 검색해야 하는 동영상 카테고리를 식별합니다. 이 매개변수는 chart 매개변수와만 함께 사용할 수 있습니다. 기본적으로 차트는 특정 카테고리로 제한되지 않습니다. 기본값은 0입니다.


/**
 * 쿼리 요청 이스케이프 처리를 위한 값
 * 공백(" ") : %2C
 * 파이프문자(|) : %7C
 * 대시(-) : ??
 * 쉼표(,) : %2C
 */

interface YouTubeApi {

    @GET("videos")
    suspend fun getTrendingVideos(
        @Query("part") part: String = PART,
        @Query("chart") chart: String = CHART,
        @Query("maxResults") maxResults: Int = MAX_RESULT,
        @Query("regionCode") regionCode: String = REGION,
        @Query("videoCategoryId") videoCategoryId: String = VIDEO_CATEGORY_ID,
        @Query("pageToken") pageToken: String? = null,
        @Query("key") apiKey: String = API_KEY
    ): TrendVideoModel

    @GET("videoCategories")
    suspend fun getCategoryIds(
        @Query("part") part: String = PART,
        @Query("regionCode") regionCode: String = REGION,
        @Query("hl") hl: String = HL,
        @Query("key") apiKey: String = API_KEY
    ): CategoryVideoModel

    @GET("channels")
    suspend fun getChannelInfo(
        @Query("part") part: String = PART,
        @Query("id") channelId: String,
        @Query("key") apiKey: String = API_KEY
    ): ChannelByCategoryModel

    /**
     * YouTube Data API를 사용하여 비디오를 검색합니다.
     *
     * @param key YouTube Data API 키
     * @param part 검색 결과에서 반환할 리소스의 파트 (snippet 단일 값)
     * @param channelId 검색할 채널의 ID
     * @param channelType 검색할 채널의 유형 (any, show 등)
     * @param maxResults 검색 결과로 반환할 최대 항목 수 (기본 5 - 0~50)
     * @param pageToken 다음 페이지의 토큰 (페이징에 사용됨)
     * @param type 검색 결과로 반환할 리소스 유형 (video, channel, playlist 등)
     * @param q 검색할 검색어를 지정합니다, 다중 검색어 검색 가능, 이스케이프 처리 [|, 검색어 추가], [-, 검색어 제외]]
     * @param order 정렬기준 (기본 relevance - date, rating, relevance, title, videoCount, viewCount)
     * @param videoCaption 자막제공여부 (any, closedCaption, none)
     * @param videoCategoryId 검색할 비디오의 카테고리 ID
     * @param videoDuration 검색할 비디오의 길이 (any, long, medium, short)
     * @param videoEmbeddable 임베드 가능한 비디오만 반환할지 여부(any, true)
     * @param videoType 영상 유형 제한 (any, episode, movie)
     * @return 비디오 검색 결과에 대한 ApiResponse<YoutubeSearchResponse>
     */
    @GET("search")
    suspend fun searchVideos(
        @Query("key") key: String,
        @Query("part") part: String,
        @Query("channelId") channelId: String?,
        @Query("channelType") channelType: String?,
        @Query("maxResults") maxResults: Int?,
        @Query("pageToken") pageToken: String?,
        @Query("type") type: String?,
        @Query("q") q: String?,
        @Query("order") order: String?,
        @Query("videoCaption") videoCaption: String?,
        @Query("videoCategoryId") videoCategoryId: String?,
        @Query("videoDuration") videoDuration: String?,
        @Query("videoEmbeddable") videoEmbeddable: Boolean?,
        @Query("videoType") videoType: String?
    ): Response<YoutubeSearchResponse>

    /**
     * YouTube Data API를 사용하여 채널의 최신 비디오를 가져옵니다.
     *
     * @param channelId 채널의 ID
     * @param maxResults 가져올 최대 비디오 수
     * @param type 검색 결과로 반환할 리소스 유형 (기본값은 "video")
     * @param order 결과 정렬 순서 (기본값은 "date")
     * @return 채널의 최신 비디오 목록에 대한 ApiResponse<YoutubeSearchResponse>
     */

    @GET("search")
    suspend fun searchRecentVideosByChannelId(
        @Query("channelId") channelId: String,
        @Query("maxResults") maxResults: Int,
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("order") order: String = "date"
    ): Response<YoutubeSearchResponse>

}