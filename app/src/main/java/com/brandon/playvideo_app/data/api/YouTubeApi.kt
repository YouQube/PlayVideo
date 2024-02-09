package com.brandon.playvideo_app.data.api

import com.brandon.playvideo_app.data.model.YoutubeSearchResponse
import retrofit2.Response
import retrofit2.http.GET
import retrofit2.http.Query

private const val PART = "snippet"
private const val CHART = "mostPopular"
private const val MAX_RESULT = 20
private const val REGION = "KR"
private const val API_KEY = "AIzaSyBK9jGXGSG50AwWRFEc35EoMvRFu0bPkWQ"


/**
 * 쿼리 요청 이스케이프 처리를 위한 값
 * 공백(" ") : %2C
 * 파이프문자(|) : %7C
 * 대시(-) : ??
 * 쉼표(,) : %2C
 */

interface YouTubeApi {


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