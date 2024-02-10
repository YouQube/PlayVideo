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

    //repository 데이터 요청 하고 통신 끝나면 isLoading false
    fun trendingVideos() {
        viewModelScope.launch {
            val trendingVideos = repository.getTrendingVideos()
            _trendVideos.value = trendingVideos
            loadingState(false)
        }
    }
    fun loadingState(state:Boolean){
        _isLoading.value = state
    }
}