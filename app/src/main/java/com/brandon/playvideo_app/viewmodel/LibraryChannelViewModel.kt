package com.brandon.playvideo_app.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.brandon.playvideo_app.data.repository.ChannelLibraryRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class LibraryChannelViewModel @Inject constructor(private val channelsRepo: ChannelLibraryRepository) : ViewModel() {

    private val searchQuery = MutableStateFlow("")

    val channels = searchQuery.flatMapLatest { query->
        channelsRepo.channels.map { it -> it.filter { it.channelTitle?.contains(query, ignoreCase = true) == true } }
    }.stateIn(viewModelScope, SharingStarted.Eagerly, emptyList())

    fun onSearchQueryChanged(query:String) = viewModelScope.launch {
        searchQuery.emit(query)
    }

}