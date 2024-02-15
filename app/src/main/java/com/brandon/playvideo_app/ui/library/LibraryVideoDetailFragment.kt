package com.brandon.playvideo_app.ui.library

import android.app.Activity.RESULT_OK
import android.content.ContentValues.TAG
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import com.brandon.playvideo_app.R
import com.brandon.playvideo_app.data.local.entities.VideoEntity
import com.brandon.playvideo_app.databinding.LibraryVideoDetailFragmentBinding
import com.brandon.playvideo_app.model.SearchListItem
import com.brandon.playvideo_app.ui.detail.VideoDetailViewModel
import com.brandon.playvideo_app.util.Converter
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import timber.log.Timber

@AndroidEntryPoint
class LibraryVideoDetailFragment :
    Fragment(R.layout.library_video_detail_fragment) {

    private var _binding : LibraryVideoDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel by viewModels<VideoDetailViewModel>()
//    private val detailViewModel: VideoDetailViewModel by viewModels()

    private var resListItem = mutableListOf<SearchListItem>()

    private var imageUrl = ""

    private var viewCount = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().getInt(getString(R.string.videoID), -1).also {
            if(it != -1) detailViewModel.setVideoId(it)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance(item: SearchListItem) : LibraryVideoDetailFragment {
            return LibraryVideoDetailFragment().apply {
                arguments = Bundle().apply {
                    putString("searchResultTitle", item.title)
                    putString("searchResultChannel", item.uploader)
                    putString("searchResultDescription", item.description)
                    putInt("searchResultViews", item.viewCount!!)
                    putString("searchResultThumbnail", item.thumbnail)
//                    resListItem.add(SearchListItem(title,channelTitle,views,thumbnail,description, false,"0",null))
                }
            }
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        _binding = LibraryVideoDetailFragmentBinding.inflate(inflater, container, false)

        binding.tvDetailFavorite.setOnClickListener {
            if (detailViewModel.videoId == null) {
                detailViewModel.video.value?.let { saveVideo() }
            } else {
                detailViewModel.video.value?.let { deleteVideo() }
            }
        }
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        arguments?.let {
            val itemTitle = it.getString("searchResultTitle", "")
            val itemChannelTitle = it.getString("searchResultChannel", "")
            val itemDescription = it.getString("searchResultDescription", "")
            val itemViews = it.getInt("searchResultViews", 1)
            val itemThumbnail = it.getString("searchResultThumbnail", "")
            imageUrl = itemThumbnail
            viewCount = itemViews

            binding.tvVideoDetailTitle.text = itemTitle
            binding.tvVideoDetailChannelTitle.text = itemChannelTitle
            binding.tvVideoDetailDescription.text = itemDescription
            binding.tvVideoDetailViews.text = Converter.formatViews(itemViews)
            Glide.with(this@LibraryVideoDetailFragment)
                .load(itemThumbnail)
                .into(binding.ivDetailThumbnail)
        }
//        initViews()
        collectVideos()
        binding.tvDetailFavorite.setOnClickListener {
            if (binding.tvDetailFavorite.text == "delete") {
                deleteVideo()
            } else {
                saveVideo()
            }
        }
    }

    private fun collectVideos() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        detailViewModel.video.collectLatest {
            it?.let(this@LibraryVideoDetailFragment::setVideoDataInUI)
        }
    }

    private fun setVideoDataInUI(video: VideoEntity) = binding.apply {
        tvVideoDetailTitle.text = video.title
        tvVideoDetailDescription.text = video.description
        tvVideoDetailChannelTitle.text = video.channelTitle
        tvVideoDetailViews.text = Converter.formatViews(video.views)
        Glide.with(this@LibraryVideoDetailFragment)
            .load(video.url)
            .into(ivDetailThumbnail)

        if (video.isFavorite) {
            tvDetailFavorite.text = "delete"
        }
    }

    private fun updateVideo(video: VideoEntity) = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        video.apply {
            title = binding.tvVideoDetailTitle.text.toString()
            description = binding.tvVideoDetailDescription.text.toString()
            url = binding.ivDetailThumbnail.toString()
        }.also {
            detailViewModel.updateVideo(it)
        }
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun saveVideo() {
        Log.d(TAG, "save")
        val tvVideoTitle = view?.findViewById<TextView>(R.id.tv_video_detail_title)
        val tvVideoDescription = view?.findViewById<TextView>(R.id.tv_video_detail_description)
        val tvChannel = view?.findViewById<TextView>(R.id.tv_video_detail_channelTitle)
        val tvViews = view?.findViewById<TextView>(R.id.tv_video_detail_views)
        val ivVideoThumbnail = view?.findViewById<ImageView>(R.id.iv_detail_thumbnail)

        when {
            tvVideoTitle?.text.isNullOrEmpty() -> {
                Snackbar.make(
                    requireView(),
                    getString(R.string.title_require),
                    Snackbar.LENGTH_LONG
                )
                    .setAction(getString(R.string.snackbarok)) {

                    }.show()
            }

            tvVideoDescription?.text.isNullOrEmpty() -> {
                Snackbar.make(
                    requireView(),
                    getString(R.string.empty_note_description_warning),
                    Snackbar.LENGTH_LONG
                ).setAction(getString(R.string.snackbarok)) {

                }.show()
            }

            else -> {
                viewLifecycleOwner.lifecycleScope.launchWhenCreated {
                    VideoEntity().apply {
                        title = tvVideoTitle?.text.toString()
                        description = tvVideoDescription?.text.toString()
                        channelTitle = tvChannel?.text.toString()
                        views = viewCount
                        url = imageUrl
                        isFavorite = true
                    }.also {
                        detailViewModel.saveVideo(it)
                    }
                    requireActivity().supportFragmentManager.popBackStack()
                }
            }
        }
    }

    private fun deleteVideo() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        detailViewModel.deleteVideo()
        requireActivity().supportFragmentManager.popBackStack()
    }

    fun onRationaleAccepted(requestCode: Int) {
    }

    fun onRationaleDenied(requestCode: Int) {
    }
}
