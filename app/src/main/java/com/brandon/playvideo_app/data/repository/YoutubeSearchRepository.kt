package com.brandon.playvideo_app.data.repository

import com.brandon.playvideo_app.data.model.RepositoryResult
import com.brandon.playvideo_app.data.model.YoutubeSearchResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * YouTube Data API를 사용하여 비디오를 검색하고 채널의 최신 영상을 가져오는 리포지토리 인터페이스.
 */
interface YoutubeSearchRepository {


    /**
     * 채널 ID를 기준으로 최근 영상을 가져오는 함수입니다.
     * @param channelId 채널 ID
     * @param maxResults 가져올 최대 영상 수 (기본값은 5)
     * @return 채널의 최근 영상 목록에 대한 RepositoryResult<YoutubeSearchResponse>
     */
    @GET("search")
    suspend fun fetchRecentVideosByChannelId(
        @Query("channelId") channelId: String,
        @Query("maxResults") maxResults: Int,
        @Query("part") part: String = "snippet",
        @Query("type") type: String = "video",
        @Query("order") order: String = "date",
        @Query("pageToken") pageToken: String? = null,
    ): RepositoryResult<YoutubeSearchResponse>






}
