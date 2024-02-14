package com.brandon.playvideo_app.ui.subscription

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brandon.playvideo_app.data.model.ChannelInfoModel
import com.brandon.playvideo_app.data.model.ChannelItemHorizontal
import com.brandon.playvideo_app.data.model.RepositoryResult
import com.brandon.playvideo_app.data.model.SubscribedChannelModel
import com.brandon.playvideo_app.data.model.SubscribedVideoModel
import com.brandon.playvideo_app.data.model.VideoEntity
import com.brandon.playvideo_app.data.model.VideoInfoModel
import com.brandon.playvideo_app.data.model.VideoItemVertical
import com.brandon.playvideo_app.data.model.toChannelItem
import com.brandon.playvideo_app.data.repository.YoutubeChannelRepository
import com.brandon.playvideo_app.data.repository.YoutubeSearchRepository
import com.brandon.playvideo_app.data.repository.YoutubeVideoRepository
import com.jess.camp.util.SingleLiveEvent
import kotlinx.coroutines.launch
import timber.log.Timber
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

class SubscriptionViewModel(
    private val youtubeSearchRepository: YoutubeSearchRepository,
    private val youtubeChannelRepository: YoutubeChannelRepository,
    private val youtubeVideoRepository: YoutubeVideoRepository,
) : ViewModel() {

    private val _event: SingleLiveEvent<SubscriptionListEvent> = SingleLiveEvent()
    val event: LiveData<SubscriptionListEvent> get() = _event

    private val _mediaData = MutableLiveData<List<SubscribedChannelModel>>(
        listOf(
            SubscribedChannelModel(channelId = "UChbZEmY6uHbTRHxu5g--c7Q"),  // 일타쿠마
//            SubscribedChannelModel(channelId = "UC0VR2v4TZeGcOrZHnmwbU_Q"),  // 육식맨
//            SubscribedChannelModel(channelId = "UCg86gCCgZGWkoHk8c015cQQ"),  // 떼잉
//        SubscribedChannelItem(channelId = "UCY2uWQDCzn_ZE-JpTfDRR2A"),  // 입질의 추억
//        SubscribedChannelItem(channelId = "UCvW8norVMTLt7QN-s2pS4Bw"),  // 조승연의 탐구생활
        )
    )
    val mediaData: LiveData<List<SubscribedChannelModel>> = _mediaData

    private val _channelsHorizontal = MutableLiveData<List<ChannelItemHorizontal>>()
    val channelItemHorizontal: LiveData<List<ChannelItemHorizontal>> = _channelsHorizontal

    private val _videosVertical = MutableLiveData<List<VideoItemVertical>>(emptyList())
    val videosVertical: LiveData<List<VideoItemVertical>> = _videosVertical

    private val channels: List<SubscribedChannelModel>? = _mediaData.value

    private var isLoading = false

    init {
        setVideos()
    }


    private fun setVideos(isAdd: Boolean = false) {
        Timber.d("구독 채널 데이터 불러오기")
        viewModelScope.launch {
            val allVideos = mutableListOf<VideoItemVertical>()
            channels?.forEach { channel ->
                val videos = if (isAdd) {
                    getNewVideos(channel, channel.nextPageToken)

                } else {
                    getNewVideos(channel)
                }
                allVideos += videos
            }
            updateChannelStates()
            updateVideos(allVideos)
            isLoading = false
        }
    }

    fun getNextVideos() {
        if (isLoading) return
        isLoading = true
        setVideos(true)
    }


    private fun updateVideos(newVideos: MutableList<VideoItemVertical>) {
        Timber.tag("count").d("기존 리스트 개수: ${videosVertical.value?.size}")
        Timber.tag("count").d("추가 리스트 개수: ${newVideos.size}")
        val allVideos = (videosVertical.value?.toMutableList() ?: emptyList()) + newVideos.shuffled()
        _videosVertical.value = allVideos
    }

    fun sortVideoList(isAscending: Boolean? = true) {
        if (isAscending == true) {
            _videosVertical.value = videosVertical.value?.sortedBy {
                LocalDateTime.parse(it.publishedAt, DateTimeFormatter.ISO_DATE_TIME)
            }
        } else {
            _videosVertical.value = videosVertical.value?.sortedByDescending {
                LocalDateTime.parse(it.publishedAt, DateTimeFormatter.ISO_DATE_TIME)
            }
        }
    }


    private fun updateChannelStates() {
        _channelsHorizontal.value =
            channels?.map { it.toChannelItem() }?.sortedByDescending { it.isActive }
    }

    private suspend fun getNewVideos(
        channel: SubscribedChannelModel,
        pageToken: String? = null
    ): List<VideoItemVertical> {
        val channelInfo = fetchChannelInfo(channel)
        channel.channelInfo = channelInfo

        val videosWithoutInfo = fetchChannelVideoIdsWithoutInfo(channel, pageToken)
        val videosInfo = fetchVideosInfo(videosWithoutInfo)
        val newVideos = videosWithoutInfo.map { video ->
            val targetInfo = videosInfo.find { it.videoId == video.videoId }
            video.copy(info = targetInfo)
        }
        // 각 채널 정보에 새 영상 추가
        channel.videos = newVideos
        // 리스트 아이템으로 반환
        return newVideos.map { video ->
            VideoItemVertical(
                videoId = video.videoId,
                videoTitle = video.info?.snippet?.title,
                channelName = video.info?.snippet?.channelTitle,
                viewers = video.info?.statistics?.viewCount,
                publishedAt = video.info?.snippet?.publishedAt,
                videoThumbnailUrl = video.info?.snippet?.thumbnails?.maxres?.url,
                channelIconImageUrl = channel.channelInfo?.snippet?.thumbnails?.default?.url,
            )
        }
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

    private suspend fun fetchChannelVideoIdsWithoutInfo(
        channel: SubscribedChannelModel, pageToken: String? = null
    ): List<SubscribedVideoModel> {
        Timber.d("fetchChannelVideos called")
        val result = if (pageToken == null) {
            Timber.tag("load").d("첫 페이지 로드")
            youtubeSearchRepository.fetchRecentVideosByChannelId(
                channelId = channel.channelId, maxResults = 10
            )
        } else {
            Timber.tag("load").d("다음 페이지 로드")
            youtubeSearchRepository.fetchRecentVideosByChannelId(
                channelId = channel.channelId, maxResults = 10, pageToken = pageToken
            )
        }
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

    fun onClickItem(id: String) {
        val videoEntity = findVideoInfoModel(id)
        _event.value = videoEntity?.let { SubscriptionListEvent.OpenContent(it) }
    }

    private fun findVideoInfoModel(id: String): VideoEntity? {
        channels?.forEach { channel ->
            val videoModel = channel.videos?.find { it.videoId == id }
            if (videoModel != null) {
                return VideoEntity(
                    channelId = channel.channelId,
                    channelTitle = channel.channelInfo?.snippet?.title,
                    channelIconImage = channel.channelInfo?.snippet?.thumbnails?.default?.url,
                    channelSubscriberCount = channel.channelInfo?.statistics?.subscriberCount,
                    videoId = videoModel.videoId,
                    videoTitle = videoModel.info?.snippet?.title,
                    videoCommentCount = videoModel.info?.statistics?.commentCount,
                    videoThumbnail = videoModel.info?.snippet?.thumbnails?.maxres?.url,
                    videoViewCount = videoModel.info?.statistics?.viewCount,
                    videoPublishedAt = videoModel.info?.snippet?.publishedAt,
                    videoDescription = videoModel.info?.snippet?.description
                )
            }
        }
        return null
    }

    fun swipeRefresh() {
        _videosVertical.value = videosVertical.value?.drop(5)
    }


}