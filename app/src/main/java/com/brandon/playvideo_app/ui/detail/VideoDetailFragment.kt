package com.brandon.playvideo_app.ui.detail

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.graphics.BitmapFactory
import android.net.Uri
import android.os.Bundle
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
import com.brandon.playvideo_app.databinding.VideoDetailFragmentBinding
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest

@AndroidEntryPoint
class VideoDetailFragment :
    Fragment(R.layout.video_detail_fragment) {

    private var _binding : VideoDetailFragmentBinding? = null
    private val binding get() = _binding!!

    private val detailViewModel by viewModels<VideoDetailViewModel>()
//    private val detailViewModel: VideoDetailViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        requireArguments().getInt(getString(R.string.videoID), -1).also {
            if(it != -1) detailViewModel.setVideoId(it)
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            VideoDetailFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = VideoDetailFragmentBinding.inflate(inflater, container, false)

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

//        initViews()
        collectNotes()
    }

    private fun collectNotes() = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        detailViewModel.video.collectLatest {
            it?.let(this@VideoDetailFragment::setVideoDataInUI)
        }
    }

    private fun setVideoDataInUI(video: VideoEntity) = binding.apply {
        tvDetailTitle.text = video.title
        tvDetailDescription.text = video.description
        tvDetailChannel.text = video.channelTitle
        tvDetailViews.text = video.views.toString()+"회"
        Glide.with(this@VideoDetailFragment)
            .load(video.url)
            .into(ivDetailThumbnail)

        if (video.isFavorite) {
            tvDetailFavorite.text = "delete"
        }
    }

    private fun updateVideo(video: VideoEntity) = viewLifecycleOwner.lifecycleScope.launchWhenStarted {
        video.apply {
            title = binding.tvDetailTitle.text.toString()
            description = binding.tvDetailDescription.text.toString()
            url = binding.ivDetailThumbnail.toString()
        }.also {
            detailViewModel.updateVideo(it)
        }
        requireActivity().supportFragmentManager.popBackStack()
    }

    private fun saveVideo() {

        val tvVideoTitle = view?.findViewById<TextView>(R.id.tv_detail_title)
        val tvVideoDescription = view?.findViewById<TextView>(R.id.tv_detail_description)
        val tvChannel = view?.findViewById<TextView>(R.id.tv_detail_channel)
        val tvViews = view?.findViewById<TextView>(R.id.tv_detail_views)
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
                        views = tvViews?.text.toString().toInt()
                        url = ivVideoThumbnail.toString()
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

    fun replaceFragment(fragment: Fragment, isTransition: Boolean) {

        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()

        if (isTransition) {
            fragmentTransition.setCustomAnimations(
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left
            )
        }
        fragmentTransition.replace(R.id.nav_host_fragment_activity_main, fragment)
            .addToBackStack(fragment.javaClass.simpleName)
        fragmentTransition.commit()
    }

    private fun getPathFromUri(contentUri: Uri): String? {
        var filePath: String?
        val cursor = requireActivity().contentResolver.query(contentUri, null, null, null, null)
        if (cursor == null) {
            filePath = contentUri.path
        } else {
            cursor.moveToFirst()
            val index = cursor.getColumnIndex("_data")
            filePath = cursor.getString(index)
            cursor.close()
        }
        return filePath
    }

    // Setup About Image
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (resultCode == RESULT_OK) {
            if (data != null) {

                val selectedImageUrl = data.data

                if (selectedImageUrl != null) {
                    try {

                        val inputStream =
                            requireActivity().contentResolver.openInputStream(selectedImageUrl)
                        val bitmap = BitmapFactory.decodeStream(inputStream)
                        binding.ivDetailThumbnail.setImageBitmap(bitmap)
                    } catch (e: Exception) {
                        Toast.makeText(requireContext(), e.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    fun onRationaleAccepted(requestCode: Int) {
    }

    fun onRationaleDenied(requestCode: Int) {
    }
}
