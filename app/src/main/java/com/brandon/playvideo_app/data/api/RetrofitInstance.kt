package com.brandon.playvideo_app.data.api

import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"

object RetrofitInstance {
    private val retrofit by lazy {
        Retrofit.Builder().baseUrl(BASE_URL)
            .client(OkHttpClient.Builder().addInterceptor(HttpLoggingInterceptor().apply {
                level = HttpLoggingInterceptor.Level.BODY
            }).build()).addConverterFactory(GsonConverterFactory.create()).build()
    }
    val api: YouTubeApi by lazy { retrofit.create(YouTubeApi::class.java) }

}