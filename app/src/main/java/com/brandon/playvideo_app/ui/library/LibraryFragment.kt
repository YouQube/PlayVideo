package com.brandon.playvideo_app.ui.library

import android.content.Context
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.graphics.drawable.Drawable
import android.os.Bundle
import android.util.Base64
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.graphics.drawable.toBitmap
import androidx.fragment.app.viewModels
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.brandon.playvideo_app.R
import com.brandon.playvideo_app.databinding.LibraryFragmentBinding
import com.brandon.playvideo_app.databinding.ToolbarCommonBinding
import com.brandon.playvideo_app.ui.library.adapter.LibraryChannelAdapter
import com.brandon.playvideo_app.ui.library.adapter.LibraryVideoAdapter
import com.brandon.playvideo_app.ui.search.SearchFragment
import com.brandon.playvideo_app.viewmodel.LibraryChannelViewModel
import com.brandon.playvideo_app.viewmodel.LibraryVideoViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import timber.log.Timber
import java.io.ByteArrayOutputStream

@AndroidEntryPoint
class LibraryFragment : Fragment() {

    private var _binding : LibraryFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var libraryVideoAdapter: LibraryVideoAdapter
    private lateinit var libraryChannelAdapter: LibraryChannelAdapter
//    private lateinit var adapter: EditProfileAdapter

    private val videoViewModel by viewModels<LibraryVideoViewModel>()

    private val channelViewModel by viewModels<LibraryChannelViewModel>()

    private val sharedPreferences by lazy {
        requireContext().getSharedPreferences("sp", Context.MODE_PRIVATE)
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            LibraryFragment().apply {
                arguments = Bundle().apply {
                }
            }

        const val KEY_PREFS = "profile_setting"
        const val EXTRA_STORE_PROFILE_IMAGE = "storeProfileImage"
    }

    override fun onResume() {
        super.onResume()
        loadUserProfile()
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?,
    ): View {
        // Inflate the layout for this fragment
        _binding = LibraryFragmentBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(view)
        setUpRv()
        collectVideos()
        collectChannels()
        loadUserProfile()

        binding.channelList.setOnClickListener {
//            detailChannelFragment(DetailChannelFragment.newInstance(), true)
        }

        binding.ivEditProfile.setOnClickListener {
            val intent = Intent(activity, EditProfileActivity::class.java)
            startActivity(intent)
        }

    }

    private val onVideoClicked = object : LibraryVideoAdapter.OnItemClickListener {
        override fun onClicked(videosId: Int) {

            val fragment: Fragment
            val bundle = Bundle()
            bundle.putInt(getString(R.string.videoID), videosId)
            fragment = LibraryVideoDetailFragment()
            fragment.arguments = bundle

            replaceFragment(fragment, true)
        }
    }

    private val onChannelClicked = object : LibraryChannelAdapter.OnItemClickListener {
        override fun onClicked(videosId: Int) {

            val fragment: Fragment
            val bundle = Bundle()
            bundle.putInt(getString(R.string.videoID), videosId)
//            fragment = DetailFragment.newInstance()
//            fragment.arguments = bundle
//
//            replaceFragment(fragment, true)
        }
    }

    private fun collectVideos() = viewLifecycleOwner.lifecycleScope.launch {
        videoViewModel.videos.collectLatest {
            libraryVideoAdapter.submitList(it)
        }
    }

    private fun collectChannels() = viewLifecycleOwner.lifecycleScope.launch {
        channelViewModel.channels.collectLatest {
            libraryChannelAdapter.submitList(it)
        }
    }

    private fun setUpRv() = binding.apply {
        libraryVideoAdapter = LibraryVideoAdapter().apply { setOnClickListener(onVideoClicked) }
        videoList.layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
        videoList.adapter = libraryVideoAdapter

        libraryChannelAdapter =
            LibraryChannelAdapter().apply { setOnClickListener(onChannelClicked) }
        channelList.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        channelList.adapter = libraryChannelAdapter
    }

    fun replaceFragment(fragment: Fragment, isTransition: Boolean) {

        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()

        fragmentTransition.replace(R.id.nav_host_fragment_activity_main, fragment)
            .addToBackStack(fragment.javaClass.simpleName)
        fragmentTransition.commit()
    }

    fun updateData(profileImage: Drawable, name: String, description: String) {
        with(binding) {
            ivEditProfile.setImageDrawable(profileImage)
            tvProfileName.text = name
            tvProfileDescription.text = description
        }
        saveUserProfile(profileImage, name, description)
    }

    fun giveImageData(): Drawable {
        return binding.ivEditProfile.drawable
    }

    private fun saveUserProfile(profileImage: Drawable, name: String, description: String) {
        val stream = ByteArrayOutputStream()
        profileImage.toBitmap()
            .compress(Bitmap.CompressFormat.PNG, 100, stream) // Drawable을 Bitmap으로 변환
        val byteArray = stream.toByteArray()
        val editor = sharedPreferences.edit()
        editor.putString(
            EXTRA_STORE_PROFILE_IMAGE,
            Base64.encodeToString(byteArray, Base64.DEFAULT)
        ) // Bitmap을 Base64 문자열로 인코딩
        editor.putString("name", name)
        editor.putString("description", description)
        editor.apply()
    }

    private fun loadUserProfile() {
        val storedProfileImage = sharedPreferences.getString(EXTRA_STORE_PROFILE_IMAGE, "")
        if (storedProfileImage != "") {
            val byteArray =
                Base64.decode(storedProfileImage, Base64.DEFAULT) // Base64 문자열을 Bitmap으로 디코딩
            val bitmap = BitmapFactory.decodeByteArray(byteArray, 0, byteArray.size)
            with(binding) {
                ivEditProfile.setImageBitmap(bitmap) // Bitmap을 이용해 이미지 띄우기
                tvProfileName.text = sharedPreferences.getString("name", "")
                tvProfileDescription.text = sharedPreferences.getString("description", "")
            }
        }
    }

    private fun setupToolbar(view: View) {
        val toolbarBinding = ToolbarCommonBinding.bind(view.findViewById(R.id.included_tool_bar))
        toolbarBinding.toolbarCommon.inflateMenu(R.menu.common_tool_bar_menu)
        toolbarBinding.toolbarCommon.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    Timber.d("Search Item Clicked!")
                    true
                }

                else -> false
            }
        }
    }
}