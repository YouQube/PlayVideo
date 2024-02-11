package com.brandon.playvideo_app.ui.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brandon.playvideo_app.data.model.RepositoryResult
import com.brandon.playvideo_app.data.model.SubscribedChannelItem
import com.brandon.playvideo_app.data.repository.YoutubeSearchRepository
import kotlinx.coroutines.launch
import timber.log.Timber

class SubscriptionViewModel(
    private val youtubeSearchRepository: YoutubeSearchRepository
) : ViewModel() {

    // 비디오, 카테고리, 채널, 써치

    /**
     * 구독 채널 정보
     * 구독 채널의 이름, 영상 정보, 전채 영상 개수, 마지막 개수 업데이트 시간 정보 데이터
     * 초기 구독 채널의 id 만 가지고 있다. DB 에서 불러옴
     * 이후 api call 로 channelInfo 필드 값을 채우고
     * 그 중 totalVideoCount 와 lastCountUpdatedTime을 업데이트 해야한다
     */
    val channels: List<SubscribedChannelItem> = listOf(
        SubscribedChannelItem(channelId = "UChbZEmY6uHbTRHxu5g--c7Q"),  // 일타쿠마
        SubscribedChannelItem(channelId = "UC0VR2v4TZeGcOrZHnmwbU_Q"),  // 육식맨
        SubscribedChannelItem(channelId = "UCg86gCCgZGWkoHk8c015cQQ"),  // 떼잉
        SubscribedChannelItem(channelId = "UCY2uWQDCzn_ZE-JpTfDRR2A"),  // 입질의 추억
        SubscribedChannelItem(channelId = "UCvW8norVMTLt7QN-s2pS4Bw"),  // 조승연의 탐구생활
    )

}