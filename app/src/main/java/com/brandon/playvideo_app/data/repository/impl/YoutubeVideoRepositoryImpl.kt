package com.brandon.playvideo_app.data.repository.impl

import com.brandon.playvideo_app.data.api.YouTubeApi
import com.brandon.playvideo_app.data.model.RepositoryResult
import com.brandon.playvideo_app.data.model.YoutubeVideoResponse
import com.brandon.playvideo_app.data.repository.YoutubeVideoRepository
import com.brandon.playvideo_app.util.RetrofitUtil.error
import timber.log.Timber

class YoutubeVideoRepositoryImpl(private val youtubeApi: YouTubeApi) : YoutubeVideoRepository {

    override suspend fun fetchVideoInfoById(
        videoId: String,
        part: String?
    ): RepositoryResult<YoutubeVideoResponse> {
        Timber.d("fetchVideoInfoById 호출됨")
        return try {
            val response = youtubeApi.videos(videoId = videoId)
            if (response.isSuccessful) {
                val data = response.body()
                if (data != null) {
                    RepositoryResult.Success(data = data)
                } else {
                    RepositoryResult.Error(-1, "응답 본문이 null입니다")
                }
            } else {
                RepositoryResult.Error(
                    response.code(), response.error()
                )
            }

        } catch (e: Exception) {
            RepositoryResult.Error(-1, e.message ?: "알 수없는 오류")
        }
    }

    override suspend fun fetchVideoInfoByIds(
        videoIds: List<String>,
        part: String?
    ): RepositoryResult<YoutubeVideoResponse> {
        Timber.d("fetchVideoInfoByIds 호출됨")
        val joinedString =videoIds.joinToString(separator = ",")
        return fetchVideoInfoById(joinedString)
    }


}