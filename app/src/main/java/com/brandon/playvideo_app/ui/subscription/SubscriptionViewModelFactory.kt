package com.brandon.playvideo_app.ui.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brandon.playvideo_app.data.repository.YoutubeChannelRepository
import com.brandon.playvideo_app.data.repository.YoutubeSearchRepository
import com.brandon.playvideo_app.data.repository.YoutubeVideoRepository

class SubscriptionViewModelFactory(
    private val youtubeSearchRepository: YoutubeSearchRepository,
    private val youtubeChannelRepository: YoutubeChannelRepository,
    private val youtubeVideoRepository: YoutubeVideoRepository,
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SubscriptionViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SubscriptionViewModel(youtubeSearchRepository, youtubeChannelRepository, youtubeVideoRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}