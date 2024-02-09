package com.brandon.playvideo_app.ui.subscription

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.brandon.playvideo_app.data.repository.YoutubeSearchRepository

class SubscriptionViewModelFactory(
    private val youtubeSearchRepository: YoutubeSearchRepository
) : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if(modelClass.isAssignableFrom(SubscriptionViewModel::class.java)){
            @Suppress("UNCHECKED_CAST")
            return SubscriptionViewModel(youtubeSearchRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}