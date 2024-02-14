package com.brandon.playvideo_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brandon.playvideo_app.data.repository.VideoLibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryVideoViewModel @Inject constructor(private val videosRepo: VideoLibraryRepository) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val videos = searchQuery.flatMapLatest { query->
        videosRepo.videos.map { it -> it.filter { it.title?.contains(query, ignoreCase = true) == true } }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onSearchQueryChanged(query:String) = viewModelScope.launch {
        searchQuery.emit(query)
    }
}