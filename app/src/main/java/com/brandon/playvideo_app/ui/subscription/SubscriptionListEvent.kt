package com.brandon.playvideo_app.ui.subscription

import com.brandon.playvideo_app.data.model.VideoEntity

interface SubscriptionListEvent {

    data class OpenContent(
        val videoEntity: VideoEntity,
    ) : SubscriptionListEvent

}