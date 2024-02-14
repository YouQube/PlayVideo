package com.brandon.playvideo_app.ui.detail

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brandon.playvideo_app.data.local.entities.VideoEntity
import com.brandon.playvideo_app.data.repository.VideoLibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class VideoDetailViewModel @Inject constructor(private val videosRepo: VideoLibraryRepository) : ViewModel() {

    val videoId = MutableStateFlow<Int?>(null)

    val video = videoId.flatMapLatest {
        val video = it?.let { videosRepo.getVideo(it) }
        flowOf(video)
    }.stateIn(viewModelScope, SharingStarted.Eagerly, null)

    fun setVideoId(id:Int) = viewModelScope.launch {
        videoId.emit(id)
    }

    suspend fun updateVideo(videoEntity: VideoEntity) = videosRepo.updateVideos(videoEntity)

    suspend fun saveVideo(videoEntity: VideoEntity) = videosRepo.insertVideo(videoEntity)

    suspend fun deleteVideo() = videoId.value?.let { videosRepo.deleteVideoById(it) }

}