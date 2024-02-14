package com.brandon.playvideo_app.ui.subscription

data class SubscriptionUiState(
    val isVideoLoading: Boolean,
    val isInitialLoading: Boolean
) {
    companion object {
        fun init() = SubscriptionUiState(
            isVideoLoading = true,
            isInitialLoading = true
        )
    }
}