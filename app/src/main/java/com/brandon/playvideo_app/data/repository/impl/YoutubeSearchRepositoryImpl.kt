package com.brandon.playvideo_app.data.repository.impl

import com.brandon.playvideo_app.data.api.YouTubeApi
import com.brandon.playvideo_app.data.model.RepositoryResult
import com.brandon.playvideo_app.data.model.YoutubeSearchResponse
import com.brandon.playvideo_app.data.repository.YoutubeSearchRepository
import retrofit2.http.Query
import timber.log.Timber

/**
 * YouTube API와 통신하는 YoutubeSearchRepository의 구현체입니다.
 * 이 클래스는 네트워크 작업에 Retrofit을 활용합니다.
 *
 * @param youtubeApi API 요청을 만드는 YouTubeApi 인터페이스의 인스턴스
 */
class YoutubeSearchRepositoryImpl(private val youtubeApi: YouTubeApi) : YoutubeSearchRepository {

    /**
     * 제공된 쿼리 매개변수에 기반하여 비디오를 검색합니다.
     *
     * @param part API 응답에 포함될 하나 이상의 검색 리소스 속성을 지정합니다.
     * @param channelId API 응답이 해당 매개변수에서 지정된 채널에 의해 생성된 리소스만 포함하도록 지정합니다.
     * @param channelType 검색을 특정 유형의 채널로 제한합니다.
     * @param maxResults 결과 세트에 반환될 최대 항목 수를 지정합니다.
     * @param pageToken 결과 세트의 특정 페이지를 식별합니다.
     * @param type 검색 쿼리를 특정 리소스 유형만 검색하도록 제한합니다.
     * @param q 검색할 쿼리 용어를 지정합니다.
     * @param order API 응답이 나열할 리소스의 순서를 지정합니다.
     * @param videoCaption 자막이 있는 비디오만 검색 결과로 포함하도록 필터링합니다.
     * @param videoCategoryId 검색을 특정 카테고리 ID로 제한합니다.
     * @param videoDuration 비디오 검색 결과를 기간에 따라 필터링합니다.
     * @param videoEmbeddable 웹 페이지에 임베드 할 수있는 비디오 만 검색 결과로 제한합니다.
     * @param videoType 비디오 유형에 따라 검색을 제한합니다.
     *
     * @return 검색 작업의 결과를 포함하는 RepositoryResult 객체
     */
    override suspend fun searchVideos(
        part: String,
        channelId: String?,
        channelType: String?,
        maxResults: Int?,
        pageToken: String?,
        type: String?,
        q: String?,
        order: String?,
        videoCaption: String?,
        videoCategoryId: String?,
        videoDuration: String?,
        videoEmbeddable: Boolean?,
        videoType: String?
    ): RepositoryResult<YoutubeSearchResponse> {
        TODO("Not yet implemented")
    }

    /**
     * channelId로 식별된 채널이 업로드 한 최근 비디오를 검색합니다.
     *
     * @param channelId 검색 결과를 반환 할 채널의 ID입니다.
     * @param maxResults 결과 세트에 반환될 최대 항목 수를 지정합니다.
     * @param part API 응답에 포함될 하나 이상의 검색 리소스 속성을 지정합니다.
     * @param type 검색 쿼리를 특정 리소스 유형만 검색하도록 제한합니다.
     * @param order API 응답이 나열할 리소스의 순서를 지정합니다.
     *
     * @return API 요청의 결과를 포함하는 RepositoryResult 객체
     */
    override suspend fun getRecentVideosByChannelId(
        @Query(value = "channelId") channelId: String,
        @Query(value = "maxResults") maxResults: Int,
        @Query(value = "part") part: String,
        @Query(value = "type") type: String,
        @Query(value = "order") order: String
    ): RepositoryResult<YoutubeSearchResponse> {
        Timber.d("getRecentVideosByChannelId 호출됨")
        return try {
            val response = youtubeApi.searchRecentVideosByChannelId(channelId, maxResults, part, type, order)
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    RepositoryResult.Success(data = data)
                } else {
                    RepositoryResult.Error(-1, "응답 본문이 null입니다")
                }
            } else {
                RepositoryResult.Error(
                    response.code(),
                    response.message()
                )
            }
        } catch (e: Exception) {
            RepositoryResult.Error(-1, e.message ?: "알 수없는 오류")
        }
    }
}
