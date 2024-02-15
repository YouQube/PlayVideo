package com.brandon.playvideo_app.ui.detail.video

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.brandon.playvideo_app.data.model.VideoEntity
import com.brandon.playvideo_app.databinding.DetailVideoFragmentBinding
import com.brandon.playvideo_app.util.Converter.formatPublishedTime
import com.brandon.playvideo_app.util.Converter.formatSubscriberCount
import com.brandon.playvideo_app.util.Converter.formatViews
import com.bumptech.glide.Glide
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.options.IFramePlayerOptions


class VideoDetailFragment() : Fragment() {

    private var _binding: DetailVideoFragmentBinding? = null
    private val binding get() = _binding!!

    private var videoEntity: VideoEntity? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // arguments에서 데이터 가져오기
        videoEntity = arguments?.getParcelable(VIDEO_ENTITY, VideoEntity::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailVideoFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initView()
    }

    private fun initView() {
        with(binding) {
            videoEntity?.let {
                lifecycle.addObserver(youtubePlayer)
                youtubePlayer.addYouTubePlayerListener(object : AbstractYouTubePlayerListener() {
                    override fun onReady(youTubePlayer: YouTubePlayer) {
                        val videoId = "${it.videoId}"
                        youTubePlayer.loadVideo(videoId, 0f)
                    }
                })

                tvTitle.text = it.videoTitle
                tvDetailViews.text = formatViews(it.videoViewCount)
                tvPublishedAt.text = formatPublishedTime(it.videoPublishedAt)
                Glide.with(this@VideoDetailFragment)
                    .load(it.channelIconImage)
                    .into(ivChannelIcon)
                tvChannelName.text = it.channelTitle
                tvSubscriberCount.text = formatSubscriberCount(it.channelSubscriberCount?.toInt())
                tvCommentCount.text = it.videoCommentCount.toString()
                tvSummeryContent.text = it.videoDescription
            }
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        binding.youtubePlayer.release();
        _binding = null
    }


    companion object {
        private const val VIDEO_ENTITY = "video_model"

        @JvmStatic
        fun newInstance(videoEntity: VideoEntity) = VideoDetailFragment().apply {
            arguments = Bundle().apply {
                // 데이터를 arguments에 저장
                putParcelable(VIDEO_ENTITY, videoEntity)
            }
        }
    }


}