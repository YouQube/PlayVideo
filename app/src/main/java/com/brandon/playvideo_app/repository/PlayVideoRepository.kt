package com.brandon.playvideo_app.repository

import com.brandon.playvideo_app.data.api.RetrofitInstance
import com.brandon.playvideo_app.data.model.Item
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayVideoRepository {
    //api 통신 부분
    suspend fun getTrendingVideos(): List<Item> =
        withContext(Dispatchers.IO) {
            RetrofitInstance.api.getTrendingVideos().items
        }
}
