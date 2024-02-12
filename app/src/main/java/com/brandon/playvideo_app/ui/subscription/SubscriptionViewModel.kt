package com.brandon.playvideo_app.ui.subscription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brandon.playvideo_app.data.model.ChannelInfoModel
import com.brandon.playvideo_app.data.model.RepositoryResult
import com.brandon.playvideo_app.data.model.SubscribedChannelModel
import com.brandon.playvideo_app.data.model.SubscribedVideoModel
import com.brandon.playvideo_app.data.model.VideoInfoModel
import com.brandon.playvideo_app.data.repository.YoutubeChannelRepository
import com.brandon.playvideo_app.data.repository.YoutubeSearchRepository
import com.brandon.playvideo_app.data.repository.YoutubeVideoRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class SubscriptionViewModel(
    private val youtubeSearchRepository: YoutubeSearchRepository,
    private val youtubeChannelRepository: YoutubeChannelRepository,
    private val youtubeVideoRepository: YoutubeVideoRepository,
) : ViewModel() {

    private val _mediaData = MutableLiveData<List<SubscribedChannelModel>>(
        listOf(
            SubscribedChannelModel(channelId = "UChbZEmY6uHbTRHxu5g--c7Q"),  // 일타쿠마
//            SubscribedChannelItem(channelId = "UC0VR2v4TZeGcOrZHnmwbU_Q"),  // 육식맨
//        SubscribedChannelItem(channelId = "UCg86gCCgZGWkoHk8c015cQQ"),  // 떼잉
//        SubscribedChannelItem(channelId = "UCY2uWQDCzn_ZE-JpTfDRR2A"),  // 입질의 추억
//        SubscribedChannelItem(channelId = "UCvW8norVMTLt7QN-s2pS4Bw"),  // 조승연의 탐구생활
        )
    )
    val mediaData: LiveData<List<SubscribedChannelModel>> = _mediaData

    val channels: List<SubscribedChannelModel>? = _mediaData.value


    fun updateChannels() {
        // 전체 채널 순회
        // 채널의 정보 가져오기
        // 채널의 영상 리스트 id들 가져오기
        // 영상 리스트의 상세 정보 가져오기
        channels?.forEach { channel ->
            viewModelScope.launch {
                val channelInfo: ChannelInfoModel? = fetchChannelInfo(channel)
                channel.channelInfo = channelInfo

                val videosWithoutInfo: List<SubscribedVideoModel> = fetchChannelVideos(channel)
                val videosInfo: List<VideoInfoModel> = fetchVideosInfo(videosWithoutInfo)
                val videosWithInfo = videosWithoutInfo.map { video ->
                    val targetInfo = videosInfo.find { it.videoId == video.videoId }
                    video.copy(info= targetInfo)
                }
                channel.videos = videosWithInfo
            }
        }
    }

    private suspend fun fetchVideosInfo(initialVideos: List<SubscribedVideoModel>) :List<VideoInfoModel> {
        val videoIds = initialVideos.map { it.videoId }
        val result = youtubeVideoRepository.fetchVideoInfoByIds(videoIds)

        return when (result) {
            is RepositoryResult.Success -> {
                result.data.items.map { fetchedVideoInfo ->
                    VideoInfoModel(
                        fetchedVideoInfo.id,
                        fetchedVideoInfo.snippet,
                        fetchedVideoInfo.statistics,
                        fetchedVideoInfo.contentDetails
                    )
                }
            }

            is RepositoryResult.Error -> emptyList()
        }
    }

    private suspend fun fetchChannelVideos(channel: SubscribedChannelModel): List<SubscribedVideoModel> {
        Timber.d("fetchChannelVideos called")
        val result = youtubeSearchRepository.fetchRecentVideosByChannelId(channel.channelId, 10)
        return when (result) {
            is RepositoryResult.Success -> {
                // 각 채널 별  nextPageToken 업데이트
                channel.nextPageToken = result.data.nextPageToken.toString()
                // 채널의 video 영상 가져오기
                result.data.items?.map { item ->
                    item.id?.videoId.let {
                        it?.let {
                            SubscribedVideoModel(it, null)
                        }
                    }
                }?.filterNotNull() ?: emptyList()
            }

            is RepositoryResult.Error -> {
                Timber.e("Can't find the Videos from the Channel: ${channel.channelId}")
                emptyList()
            }
        }
    }

    private suspend fun fetchChannelInfo(channel: SubscribedChannelModel): ChannelInfoModel? {
        Timber.d("fetchChannelInfo called")
        val result = youtubeChannelRepository.fetchChannelInfoById(channel.channelId)
        return when (result) {
            is RepositoryResult.Success -> {
                Timber.d("fetchChannelInfo data: ${result.data}")
                result.data.items.firstOrNull()?.let {
                    ChannelInfoModel(channelId = it.id, snippet = it.snippet, statistics = it.statistics)
                }
            }

            is RepositoryResult.Error -> {
                Timber.e("Can't fetch Channel info: ${result.code}, ${result.message}")
                null
            }
        }
    }

}