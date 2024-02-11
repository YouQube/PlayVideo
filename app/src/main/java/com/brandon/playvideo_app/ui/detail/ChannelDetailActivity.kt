package com.brandon.playvideo_app.ui.detail


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brandon.playvideo_app.data.model.YoutubeChannelResponse
import com.brandon.playvideo_app.databinding.ChanneldetailActivityBinding
class ChannelDetailActivity: AppCompatActivity() {

    private val binding: ChanneldetailActivityBinding by lazy { ChanneldetailActivityBinding.inflate(layoutInflater) }
    private lateinit var channelInfo : YoutubeChannelResponse

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding

    }
}