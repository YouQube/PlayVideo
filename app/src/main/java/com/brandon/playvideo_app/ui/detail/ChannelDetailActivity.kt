package com.brandon.playvideo_app.ui.detail


import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.brandon.playvideo_app.data.model.ChannelByCategoryModel
import com.brandon.playvideo_app.databinding.ChanneldetailActivityBinding
class ChannelDetailActivity: AppCompatActivity() {

    private val binding: ChanneldetailActivityBinding by lazy { ChanneldetailActivityBinding.inflate(layoutInflater) }
    private lateinit var channelInfo : ChannelByCategoryModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(binding.root)

        binding

    }
}