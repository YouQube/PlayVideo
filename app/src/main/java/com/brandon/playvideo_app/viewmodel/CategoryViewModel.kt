package com.brandon.playvideo_app.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brandon.playvideo_app.data.model.CategoryItem
import com.brandon.playvideo_app.data.model.ChannelItem
import com.brandon.playvideo_app.data.model.Item
import com.brandon.playvideo_app.repository.PlayVideoRepository
import kotlinx.coroutines.launch

class CategoryViewModel(val repository: PlayVideoRepository = PlayVideoRepository()) : ViewModel() {
    private val _trendVideos = MutableLiveData<List<Item>>()
    val trendVideos: LiveData<List<Item>> get() = _trendVideos

    private val _categoryVideos = MutableLiveData<List<Item>>()
    val categoryVideos: LiveData<List<Item>> get() = _categoryVideos

    private val _channel = MutableLiveData<List<ChannelItem>>()
    val channel: LiveData<List<ChannelItem>> get() = _channel

    //카테고리의 ID
    private val _categoryIdList = MutableLiveData<List<CategoryItem>>()
    val categoryIdList: LiveData<List<CategoryItem>> get() = _categoryIdList

    //채널 고유의 ID
    private val channelIdList = mutableListOf<String>()
    private val _isLoading = MutableLiveData<Boolean>(true)
    val isLoading: LiveData<Boolean> get() = _isLoading
    private val _receptionState = MutableLiveData<Boolean>(true)
    val receptionState: LiveData<Boolean> get() = _receptionState
    private val _initState = MutableLiveData<Boolean>(true)
    val initState: LiveData<Boolean> get() = _initState
    private val _saveCategoryTitle = MutableLiveData<String>()
    val saveCategoryTitle: LiveData<String> get() = _saveCategoryTitle

    //칩 눌렀을 때 카테고리 별 영상
    fun fetchCategoryVideos(videoCategoryId: String) {
        viewModelScope.launch {
            runCatching {
                //api 통신 videos endPoint
                val videos = repository.fetchCategoryVideos(videoCategoryId = videoCategoryId)
                _categoryVideos.value = videos

                //channelIdList가 비어 있지 않으면 초기화 후 id추가
                if (channelIdList.isNotEmpty()) channelIdList.clear()
                videos.forEach { channelIdList.add(it.snippet.channelId) }

                //통신이 끝나면 loading 끝
                _isLoading.value = false

                //api 통신 채널 정보를 가져오는 코드 channels endPoint
                val channels = repository.getChannelInfo(channelIdList)
                _channel.value = channels

                //통신 상태 처리
                receptionState(true)
            }.onFailure {

                //api 통신 예외 발생시
                receptionFailed()
                receptionState(false)
            }
        }
    }

    //트렌딩 비디오 영상 초기 화면 셋팅용
    fun getTrendingVideos() {
        viewModelScope.launch {
            val videos = repository.getTrendingVideos().items
            _trendVideos.value = videos
            _initState.value = false
        }
    }

    //카테고리의 id 받아오는 코드
    fun getCategoryIds() {
        viewModelScope.launch {
            val ids = repository.getCategoryIds()
            _categoryIdList.value = ids
        }
    }

    //로딩 상태 처리
    fun loadingState(state: Boolean) {
        _isLoading.value = state
    }

    //api 통신 실패 처리
    private fun receptionFailed() {
        _categoryVideos.value = listOf()
        _channel.value = listOf()
        _isLoading.value = false
    }

    //통신 상태
    fun receptionState(state: Boolean) {
        _receptionState.value = state
    }

    fun saveCategoryTitle(category: String) {
        _saveCategoryTitle.value = category
    }
}