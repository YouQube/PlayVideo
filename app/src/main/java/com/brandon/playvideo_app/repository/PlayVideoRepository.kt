package com.brandon.playvideo_app.repository

import com.brandon.playvideo_app.data.api.RetrofitInstance
import com.brandon.playvideo_app.data.model.CategoryItem
import com.brandon.playvideo_app.data.model.ChannelItem
import com.brandon.playvideo_app.data.model.Item
import com.brandon.playvideo_app.data.model.YoutubeVideoResponse
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class PlayVideoRepository {
    //api 통신 부분
    suspend fun getTrendingVideos(): YoutubeVideoResponse =
        withContext(Dispatchers.IO) {
            RetrofitInstance.api.getTrendingVideos()
        }

    //칩 선택시 카테고리 영상
    suspend fun fetchCategoryVideos(videoCategoryId: String): List<Item> =
        withContext(Dispatchers.IO) {
            RetrofitInstance.api.getTrendingVideos(videoCategoryId = videoCategoryId).items
        }

    //채널 정보
    suspend fun getChannelInfo(channelIdList: MutableList<String>): List<ChannelItem> =
        withContext(Dispatchers.IO) {
            RetrofitInstance.api.getChannelInfo(channelId = channelIdList.joinToString(",")).items
        }
    //카테고리별 id를 받아오는 코드

    suspend fun getCategoryIds(): List<CategoryItem> =
        withContext(Dispatchers.IO) {
            RetrofitInstance.api.getCategoryIds().items
        }

    //다음 페이지의 트렌딩 비디오를 받아오 옴
    suspend fun getNextTrendingVideos(nextPageToken: String): YoutubeVideoResponse =
        withContext(Dispatchers.IO) {
            RetrofitInstance.api.getTrendingVideos(pageToken = nextPageToken)
        }
}
