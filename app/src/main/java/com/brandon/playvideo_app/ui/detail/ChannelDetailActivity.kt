package com.brandon.playvideo_app.ui.detail


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.brandon.playvideo_app.data.api.RetrofitClient
import com.brandon.playvideo_app.data.model.YoutubeChannelResponse
import com.brandon.playvideo_app.databinding.ChanneldetailActivityBinding
import com.brandon.playvideo_app.model.SearchListItem
import com.brandon.playvideo_app.ui.search.adapter.SearchListAdapter
import com.bumptech.glide.Glide
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class ChannelDetailActivity: AppCompatActivity() {

    private val binding by lazy { ChanneldetailActivityBinding.inflate(layoutInflater) }
    private lateinit var channelInfo : YoutubeChannelResponse
    private val apiKey = "AIzaSyCC8wNtOt0EiqzkoudHp1P9mrOHdCc1ap4"
    private val channelId = "UCS2OAdHoLt-9T6cG9A2H49Q"
    private var playlists = mutableListOf<SearchListItem>()
    private val playlistsAdapter by lazy { SearchListAdapter(playlists) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding.rvChannelDetail.layoutManager =
            LinearLayoutManager(this, LinearLayoutManager.VERTICAL, false)
        binding.rvChannelDetail.adapter = playlistsAdapter


        lifecycleScope.launch {
            //TODO 프래그먼트에서 채널id 받아서 getChannelDetail에 넣어주기
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
                tvChannelDetailVideoCount.text = channelInfo.items[0].statistics?.videoCount
                tvChannelDetailDescription.text = channelInfo.items[0].brandingSettings?.channel?.description
            }

        }

        binding.tvChannelDetailPlaylist.setOnClickListener {
            lifecycleScope.launch {
                val result = getPlaylists(channelId)
                if( result?.pageInfo?.totalResults!! > 0 ) {
                    for (item in result?.items!!){
                        val title = item.snippet?.title
                        val uploader = item.snippet?.channelTitle
                        val url = item.snippet?.thumbnails?.default?.url
                        val videoCount =  item.contentDetails?.itemCount.toString()

                        playlists.add(SearchListItem(title,uploader,null,url,false,videoCount))
                    }
                }

                playlistsAdapter.items = playlists

                playlistsAdapter.notifyDataSetChanged()
            }
        }

        binding.tvChannelDetailShorts.setOnClickListener {

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

    private suspend fun getShorts(id: String) =
        withContext(Dispatchers.IO){
            RetrofitClient.apiService.getChannelDetail(
                apiKey = apiKey,
                part = "snippet,statistics,brandingSettings",
                id = id
            )
        }
}