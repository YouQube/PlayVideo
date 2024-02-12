import com.brandon.playvideo_app.data.api.YouTubeApi
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

private const val BASE_URL = "https://www.googleapis.com/youtube/v3/"
private const val YOUTUBE_API_KEY = "AIzaSyD1EOr7wypjcnHHfrWvPmdPkx4wn04OBk4"

object NetworkClient {

    private val loggingInterceptor = HttpLoggingInterceptor().apply {
        level = HttpLoggingInterceptor.Level.BODY // 로깅 레벨 설정 (BASIC, HEADERS, BODY)
    }

    // API Key 삽입을 위한 인터셉터
    private val apiKeyInterceptor = Interceptor { chain ->
        val original = chain.request()
        val originalHttpUrl = original.url
        val url = originalHttpUrl.newBuilder()
            .addQueryParameter("key", YOUTUBE_API_KEY)  // Youtube api key 기본 추가
            .build()
        val requestBuilder = original.newBuilder().url(url)
        val request = requestBuilder.build()
        chain.proceed(request)
    }

    // OkHttpClient 설정
    private val okHttpClient = OkHttpClient.Builder()
        .addInterceptor(apiKeyInterceptor)
//        .addInterceptor(loggingInterceptor)
        .build()

    // Retrofit 설정
    private val retrofitClient = Retrofit.Builder()
        .baseUrl(BASE_URL)
        .client(okHttpClient)
        .addConverterFactory(GsonConverterFactory.create())
        .build()

    val youtubeApiService: YouTubeApi = retrofitClient.create(YouTubeApi::class.java)
}
