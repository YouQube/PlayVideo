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
import com.brandon.playvideo_app.ui.subscription.adapter.ChannelItemHorizontal
import com.brandon.playvideo_app.ui.subscription.adapter.toChannelItem
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
            SubscribedChannelModel(channelId = "UC0VR2v4TZeGcOrZHnmwbU_Q"),  // 육식맨
            SubscribedChannelModel(channelId = "UCg86gCCgZGWkoHk8c015cQQ"),  // 떼잉
//        SubscribedChannelItem(channelId = "UCY2uWQDCzn_ZE-JpTfDRR2A"),  // 입질의 추억
//        SubscribedChannelItem(channelId = "UCvW8norVMTLt7QN-s2pS4Bw"),  // 조승연의 탐구생활
        )
    )
    val mediaData: LiveData<List<SubscribedChannelModel>> = _mediaData

    private val _channelsHorizontal = MutableLiveData<List<ChannelItemHorizontal>>()
    val channelItemHorizontal: LiveData<List<ChannelItemHorizontal>> = _channelsHorizontal

    val channels: List<SubscribedChannelModel>? = _mediaData.value

//    private val channelItemsHorizontal: List<ChannelItemHorizontal>? get() {
//        return channels?.map { it.toChannelItem() }
//    }


    fun updateChannels() {
        Timber.d("구독 채널 데이터 업데이트")
        viewModelScope.launch {
            val updatedChannels = mutableListOf<SubscribedChannelModel>()
            channels?.forEach { channel ->
                updateChannel(channel)
                updatedChannels.add(channel)
            }
            updateChannelsHorizontal(updatedChannels)
        }
    }

    private suspend fun updateChannel(channel: SubscribedChannelModel) {
        val channelInfo = fetchChannelInfo(channel)
        channel.channelInfo = channelInfo

        val videosWithoutInfo = fetchChannelVideoIdsWithoutInfo(channel)
        val videosInfo = fetchVideosInfo(videosWithoutInfo)
        val videosWithInfo = videosWithoutInfo.map { video ->
            val targetInfo = videosInfo.find { it.videoId == video.videoId }
            video.copy(info = targetInfo)
        }
        channel.videos = videosWithInfo
    }


    private fun updateChannelsHorizontal(updatedChannels: MutableList<SubscribedChannelModel>) {
        // TODO: active 기준 정렬
        Timber.d("채널 정보: ${updatedChannels.map { it.channelId }}")
        _channelsHorizontal.value = updatedChannels.map { it.toChannelItem() }
        Timber.d("가로 채널 정보 업데이트: ${channelItemHorizontal.value}")
    }

    private suspend fun fetchVideosInfo(initialVideos: List<SubscribedVideoModel>): List<VideoInfoModel> {
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

    private suspend fun fetchChannelVideoIdsWithoutInfo(channel: SubscribedChannelModel): List<SubscribedVideoModel> {
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