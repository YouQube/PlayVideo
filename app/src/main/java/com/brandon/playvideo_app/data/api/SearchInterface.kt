package com.brandon.playvideo_app.data.api

import com.brandon.playvideo_app.data.model.VideoModel
import com.brandon.playvideo_app.data.model.SearchModel
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Header
import retrofit2.http.Query

interface SearchInterface {
    @GET("v3/search")
    suspend fun searchVideo(
        @Query("key") apiKey : String,
        @Query("part") part : String,
        @Query("q") q : String,
        @Query("maxResults") maxResults : Int,
        @Query("order") order :String,
        @Query("regionCode") regionCode : String,
        @Query("type") type : String,
        @Query("videoDuration") videoDuration : String
    ) : SearchModel?

    @GET("v3/videos")
    suspend fun getViewCount(
        @Query("key") apiKey : String,
        @Query("part") part : String = "statics",
        @Query("id") id : String,
        @Query("locale") locale : String
    ) : VideoModel?

}