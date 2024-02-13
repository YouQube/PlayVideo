package com.brandon.playvideo_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brandon.playvideo_app.data.model.Item
import com.brandon.playvideo_app.repository.PlayVideoRepository
import kotlinx.coroutines.launch

class TrendViewModel(val repository: PlayVideoRepository = PlayVideoRepository()) : ViewModel() {
    private val _trendVideos = MutableLiveData<List<Item>>()
    val trendVideos: LiveData<List<Item>> get() = _trendVideos

    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _receptionImage = MutableLiveData<Boolean>(false)
    val receptionImage: LiveData<Boolean> get() = _receptionImage

    //다음 페이지 정보 관련 변수
    private val pageList: MutableList<String> = mutableListOf()
    private var pageIdx = 0

    //repository 데이터 요청 하고 통신 끝나면 isLoading false
    //최초 실행시 nextPage를 받아 와서 List에 저장
    fun trendingVideos() {
        viewModelScope.launch {
            runCatching {
                val trendingVideos = repository.getTrendingVideos().items
                _trendVideos.value = trendingVideos
                loadingState(false)
                val nextPageToken = repository.getTrendingVideos().nextPageToken
                pageList.add(nextPageToken)
                failedState(false)
            }.onFailure {
                loadingState(false)
                failedState(true) //통신 실패
            }
        }
    }

    fun loadingState(state: Boolean) {
        _isLoading.value = state
    }

    //다음 트렌딩 비디오를 받아 오는 함수
    fun getNextTrendingVideos() {
        viewModelScope.launch {
            val videos = repository.getNextTrendingVideos(pageList[pageIdx]).items
            _trendVideos.value = videos
            loadingState(false)
            //해당 페이지의 nextPageToken을 받아옴
            val nextPageToken = repository.getNextTrendingVideos(pageList[pageIdx]).nextPageToken
            pageList.add(nextPageToken)
            //페이지 index 이동
            pageIdx++
        }
    }

    //통신 불가 예외 처리 state true 이면 실패
    private fun failedState(state: Boolean) {
        _receptionImage.value = state
    }
}