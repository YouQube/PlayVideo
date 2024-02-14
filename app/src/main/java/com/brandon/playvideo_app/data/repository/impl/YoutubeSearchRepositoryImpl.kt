package com.brandon.playvideo_app.data.repository.impl

import com.brandon.playvideo_app.data.api.YouTubeApi
import com.brandon.playvideo_app.data.model.RepositoryResult
import com.brandon.playvideo_app.data.model.YoutubeSearchResponse
import com.brandon.playvideo_app.data.repository.YoutubeSearchRepository
import com.brandon.playvideo_app.util.RetrofitUtil.error
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
    override suspend fun fetchRecentVideosByChannelId(
        @Query(value = "channelId") channelId: String,
        @Query(value = "maxResults") maxResults: Int,
        @Query(value = "part") part: String,
        @Query(value = "type") type: String,
        @Query(value = "order") order: String,
        @Query(value = "pageToken") pageToken: String?,
    ): RepositoryResult<YoutubeSearchResponse> {
        Timber.d("getRecentVideosByChannelId 호출됨")
        return try {
            val response = youtubeApi.search(
                channelId = channelId,
                maxResults = maxResults,
                part = part,
                type = type,
                order = order,
                pageToken = pageToken
            )
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
                    response.error()
                )
            }
        } catch (e: Exception) {
            RepositoryResult.Error(-1, e.message ?: "알 수없는 오류")
        }
    }

}
