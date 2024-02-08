package com.brandon.playvideo_app.data

import com.brandon.playvideo_app.data.api.SearchInterface
import com.google.gson.GsonBuilder

import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

object RetrofitClient {

    private val BASE_URL = "https://www.googleapis.com/youtube/"
    val apiService: SearchInterface get() =
        instance.create(SearchInterface::class.java)

    // Retrofit 인스턴스를 초기화하고 반환한다.

    private fun createOkHttpClient(): OkHttpClient {
        val interceptor = HttpLoggingInterceptor()

        interceptor.level = HttpLoggingInterceptor.Level.BODY

        return OkHttpClient.Builder()
            .connectTimeout(20, TimeUnit.SECONDS)
            .readTimeout(20, TimeUnit.SECONDS)
            .writeTimeout(20, TimeUnit.SECONDS)
            .addNetworkInterceptor(interceptor)
            .build()
    }


    private val instance: Retrofit
        private get() {
            // Gson 객체 생성. setLenient()는 JSON 파싱이 좀 더 유연하게 처리되도록 한다.
            val gson = GsonBuilder().setLenient().create()

            // Retrofit 빌더를 사용하여 Retrofit 인스턴스 생성
            return Retrofit.Builder()
                .baseUrl(BASE_URL)  // 기본 URL 설정
                .addConverterFactory(GsonConverterFactory.create(gson)).client(createOkHttpClient())  // JSON 파싱을 위한 컨버터 추가
                .build()
        }
}