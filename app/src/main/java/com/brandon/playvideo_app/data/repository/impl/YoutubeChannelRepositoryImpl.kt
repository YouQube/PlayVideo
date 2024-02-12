package com.brandon.playvideo_app.data.repository.impl

import com.brandon.playvideo_app.data.api.YouTubeApi
import com.brandon.playvideo_app.data.model.RepositoryResult
import com.brandon.playvideo_app.data.model.YoutubeChannelResponse
import com.brandon.playvideo_app.data.repository.YoutubeChannelRepository
import com.brandon.playvideo_app.util.RetrofitUtil.error
import timber.log.Timber

class YoutubeChannelRepositoryImpl(private val youtubeApi: YouTubeApi) : YoutubeChannelRepository {

    override suspend fun fetchChannelInfoById(
        channelId: String, part: String?
    ): RepositoryResult<YoutubeChannelResponse> {
        Timber.d("fetchChannelInfoById 호출됨")
        return try {
            val response = youtubeApi.channels(channelId = channelId)
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

    override suspend fun fetchChannelInfoByIds(
        channelIds: List<String>, part: String?
    ): RepositoryResult<YoutubeChannelResponse> {
        Timber.d("fetchChannelInfoByIds 호출됨")
        val joinedString = channelIds.joinToString(separator = ",")
        return fetchChannelInfoById(joinedString)
    }
}