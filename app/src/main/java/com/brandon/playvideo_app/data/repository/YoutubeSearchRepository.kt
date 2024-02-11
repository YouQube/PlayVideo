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
     * YouTube Data API를 사용하여 비디오를 검색합니다.
     *
     * @param part 검색 결과에서 반환할 리소스의 파트 (기본값은 "snippet")
     * @param channelId 검색할 채널의 ID
     * @param channelType 검색할 채널의 유형 (옵션: "any", "show" 등)
     * @param maxResults 검색 결과로 반환할 최대 항목 수 (기본값은 5, 범위: 0~50)
     * @param pageToken 다음 페이지의 토큰 (페이징에 사용됨)
     * @param type 검색 결과로 반환할 리소스 유형 (예: "video", "channel", "playlist" 등)
     * @param q 검색할 검색어를 지정합니다. 다중 검색어 검색 가능하며, 이스케이프 처리 가능 (예: "|", "-")
     * @param order 정렬 기준 (기본값은 "relevance", 옵션: "date", "rating", "title", "videoCount", "viewCount")
     * @param videoCaption 자막 제공 여부 (옵션: "any", "closedCaption", "none")
     * @param videoCategoryId 검색할 비디오의 카테고리 ID
     * @param videoDuration 검색할 비디오의 길이 (옵션: "any", "long", "medium", "short")
     * @param videoEmbeddable 임베드 가능한 비디오만 반환할지 여부 (옵션: "any", true)
     * @param videoType 영상 유형 제한 (옵션: "any", "episode", "movie")
     * @return 비디오 검색 결과에 대한 RepositoryResult<YoutubeSearchResponse>
     */
    @GET("search")
    suspend fun searchVideos(
        @Query("part") part: String = "snippet",
        @Query("channelId") channelId: String? = null,
        @Query("channelType") channelType: String? = null,
        @Query("maxResults") maxResults: Int? = 5,
        @Query("pageToken") pageToken: String? = null,
        @Query("type") type: String? = null,
        @Query("q") q: String? = null,
        @Query("order") order: String? = "relevance",
        @Query("videoCaption") videoCaption: String? = null,
        @Query("videoCategoryId") videoCategoryId: String? = null,
        @Query("videoDuration") videoDuration: String? = null,
        @Query("videoEmbeddable") videoEmbeddable: Boolean? = null,
        @Query("videoType") videoType: String? = null
    ): RepositoryResult<YoutubeSearchResponse>

}
