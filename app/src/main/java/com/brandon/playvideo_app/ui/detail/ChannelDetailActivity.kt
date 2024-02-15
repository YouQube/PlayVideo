package com.brandon.playvideo_app.ui.detail


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import com.brandon.playvideo_app.data.api.RetrofitClient
import com.brandon.playvideo_app.data.model.YoutubeChannelResponse
import com.brandon.playvideo_app.databinding.ChanneldetailActivityBinding
import com.brandon.playvideo_app.model.SearchListItem
import com.brandon.playvideo_app.ui.detail.adapter.ChannelDetailPlallistsAdapter
import com.brandon.playvideo_app.ui.detail.adapter.ChannelDetailShortsAdapter
import com.brandon.playvideo_app.ui.search.adapter.SearchListAdapter
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber
import java.time.Duration
import java.util.Collections

class ChannelDetailActivity: AppCompatActivity() {

    private val binding by lazy { ChanneldetailActivityBinding.inflate(layoutInflater) }
    private lateinit var channelInfo : YoutubeChannelResponse
    private val apiKey = "AIzaSyCC8wNtOt0EiqzkoudHp1P9mrOHdCc1ap4"
    private val channelId = "UCS2OAdHoLt-9T6cG9A2H49Q"
    private var playlists = mutableListOf<SearchListItem>()
    private var shorts = mutableListOf<SearchListItem>()
    private var video = mutableListOf<SearchListItem>()
    private val playlistsAdapter by lazy { ChannelDetailPlallistsAdapter(playlists) }
    private val shortsAdapter by lazy { ChannelDetailShortsAdapter(playlists) }
    private var videoIds = mutableListOf<String?>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

//        binding.rvChannelDetail.layoutManager =
//            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
//        binding.rvChannelDetail.adapter = playlistsAdapter


        lifecycleScope.launch {
            channelInfo = getChannelDetail(channelId)

            Glide.with(this@ChannelDetailActivity)
                .load(channelInfo.items[0].snippet.thumbnails)
                .into(binding.ivChannelDetailBanner)

            Glide.with(this@ChannelDetailActivity)
                .load(channelInfo.items[0].brandingSettings?.image?.bannerExternalUrl)
                .into(binding.ivChannelDetailBanner)

            with(binding){
                tvChannelDetailTitle.text = channelInfo.items[0].brandingSettings?.channel?.title
                tvChannelDetailHandler.text = channelInfo.items[0].snippet?.customUrl
                tvChannelDetailSubscCount.text = channelInfo.items[0].statistics?.subscriberCount
                tvChannelDetailVideoCount.text = channelInfo.items[0].statistics?.videoCount.toString()
                tvChannelDetailDescription.text = channelInfo.items[0].brandingSettings?.channel?.description
            }

        }

        binding.tvChannelDetailPlaylist.setOnClickListener {

            binding.rvChannelDetail.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvChannelDetail.adapter = playlistsAdapter

            playlists.clear()
            videoIds.clear()

            lifecycleScope.launch {
                val result = getPlaylists(channelId)
                if( result?.pageInfo?.totalResults!! > 0 ) {
                    for (item in result?.items!!){
                        val title = item.snippet?.title
                        val uploader = item.snippet?.channelTitle
                        val description = item.snippet?.description
                        val url = item.snippet?.thumbnails?.default?.url
                        val videoCount =  item.contentDetails?.itemCount.toString()

                        playlists.add(SearchListItem(title,uploader,null,url,description, false,videoCount,null))
                    }
                }

                playlistsAdapter.items = playlists

                playlistsAdapter.notifyDataSetChanged()
            }
        }

        binding.tvChannelDetailShorts.setOnClickListener {

            binding.rvChannelDetail.layoutManager =
                GridLayoutManager(this,3)
            binding.rvChannelDetail.hasFixedSize()
            binding.rvChannelDetail.adapter = shortsAdapter

            videoIds.clear()
            shorts.clear()

            lifecycleScope.launch {
                val result = getShorts(channelId)
                if( result?.pageInfo?.totalResults!! > 0 ) {
                    for (item in result?.items!!){
                        val title = item.snippet?.title
                        val uploader = item.snippet?.channelTitle
                        val description = item.snippet?.description
                        val url = item.snippet?.thumbnails?.default?.url

                        videoIds.add((item.id?.videoId))
                        shorts.add(SearchListItem(title,uploader,null,url,description, false,"0",0))
                    }
                }
                val shortResult = getViewCount(videoIds)

                for(i in playlists.indices){
                    val viewCount = shortResult?.items?.get(i)?.statistics?.viewCount
                    if (viewCount != null) {
                        shorts[i].viewCount = viewCount
                    }
                }

                shortsAdapter.items = shorts

                shortsAdapter.notifyDataSetChanged()
            }
        }

        binding.tvChannelDetailTabVideo.setOnClickListener {
            binding.rvChannelDetail.layoutManager =
                LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
            binding.rvChannelDetail.adapter = playlistsAdapter

            videoIds.clear()
            video.clear()

            lifecycleScope.launch { //서스펜드 메소드를 사용한다면 라이프사이클 스코프 안에서는 순차적으로 시행된다.

                val result = getVideo(channelId)
                Timber.tag("test").d("result= %s", result)
                if( result?.pageInfo?.totalResults!! > 0 ) {
                    for(item in result.items!!){
                        val title = item.snippet?.title
                        val uploader = item.snippet?.channelTitle
                        val description = item.snippet?.description
                        val url = item.snippet?.thumbnails?.default?.url

                        videoIds.add(item.id?.videoId)
                        video.add(SearchListItem(title,uploader,0,url!!,description,false,"0",0))

                    }
                }

                val result2 = getDurationByVideoId(videoIds)

                for(i in video.indices){
                    val playTime = result2?.items?.get(i)?.contentDetails?.duration
                    val duration = Duration.parse(playTime)
                    val seconds = duration.seconds
                    if (playTime != null) {
                        video[i].playTime = seconds
                    }
                }

                video.removeIf {
                    it.playTime!! <= 240
                }
                playlistsAdapter.items = video

                playlistsAdapter.notifyDataSetChanged()

            }
        }

    }

    private suspend fun getChannelDetail(id: String) =
        withContext(Dispatchers.IO){
            RetrofitClient.apiService.getChannelDetail(
                apiKey = apiKey,
                part = "snippet,statistics,brandingSettings",
                id = id
            )
        }

    private suspend fun getPlaylists(id: String) =
        withContext(Dispatchers.IO){
            RetrofitClient.apiService.getPlaylistsByChannelId(
                apiKey = apiKey,
                part = "snippet,contentDetails",
                channelId = id
            )
        }

    private suspend fun getShorts(channelId: String) =
        withContext(Dispatchers.IO){
            RetrofitClient.apiService.searchShortsByChannelId(
                apiKey = apiKey,
                part = "snippet",
                channelId = channelId,
                maxResults = 50,
                order= "relevance",
                regionCode ="KR",
                type ="video",
                videoDuration = "short"
            )
        }

    private suspend fun getViewCount(ids : MutableList<String?>)=
        withContext(Dispatchers.IO){
            val conrvertedId = ids.joinToString(",")
            RetrofitClient.apiService.getViewCountByVideoId(
                apiKey= apiKey,
                part="statistics",
                id= conrvertedId
            )
        }

    private suspend fun getVideo(id: String)=
        withContext(Dispatchers.IO){
            RetrofitClient.apiService.searchVideo(
                apiKey = apiKey,
                part = "snippet",
                channelId = id,
                maxResults = 30,
                order= "relevance",
                regionCode ="KR",
                type ="video",
                videoDuration = "any")
        }

    private suspend fun getDurationByVideoId(ids : MutableList<String?>) =
        withContext(Dispatchers.IO){
            val conrvertedIds = ids.joinToString(",")
            RetrofitClient.apiService.getDurationByChannelId(
                apiKey = apiKey,
                part = "contentDetails",
                id = conrvertedIds)
        }



}
