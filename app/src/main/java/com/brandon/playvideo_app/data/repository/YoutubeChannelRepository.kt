package com.brandon.playvideo_app.data.repository

import com.brandon.playvideo_app.data.model.RepositoryResult
import com.brandon.playvideo_app.data.model.YoutubeChannelResponse
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * YouTube Data API를 통해 특정 채널 정보를 검색하는 리포지토리 인터페이스입니다.
 */
interface YoutubeChannelRepository {

    /**
     * 특정 채널의 정보를 가져오는 함수입니다.
     *
     * @param channelId 가져올 채널의 ID입니다.
     * @param part 검색 결과에 포함할 리소스의 파트를 지정합니다. 기본값은 "snippet"과 "statistics"입니다.
     *              해당 파트는 API 응답에 포함됩니다.
     *              여러 파트를 지정할 경우 각 파트를 "%2C"로 구분하여 문자열로 전달합니다.
     * @return RepositoryResult<YoutubeChannelResponse> 타입의 객체를 반환합니다.
     *         이 객체는 가져온 채널 정보에 대한 결과를 포함합니다.
     * @see com.brandon.playvideo_app.data.model.YoutubeChannelResponse
     */
    @GET("channels")
    suspend fun fetchChannelInfoById(
        @Query("id") channelId: String,
        @Query("part") part: String? = "snippet,statistics"
    ): RepositoryResult<YoutubeChannelResponse>

    /**
     * 여러 채널의 정보를 한 번에 가져오는 함수입니다.
     *
     * @param channelIds 가져올 채널들의 ID 리스트입니다.
     * @param part 검색 결과에 포함할 리소스의 파트를 지정합니다. 기본값은 "snippet"과 "statistics"입니다.
     *              해당 파트는 API 응답에 포함됩니다.
     *              여러 파트를 지정할 경우 각 파트를 "%2C"로 구분하여 문자열로 전달합니다.
     * @return RepositoryResult<YoutubeChannelResponse> 타입의 객체를 반환합니다.
     *         이 객체는 가져온 채널들의 정보에 대한 결과를 포함합니다.
     * @see com.brandon.playvideo_app.data.model.YoutubeChannelResponse
     */
    @GET("channels")
    suspend fun fetchChannelInfoByIds(
        @Query("id") channelIds: List<String>,
        @Query("part") part: String? = "snippet,statistics"
    ): RepositoryResult<YoutubeChannelResponse>
}
