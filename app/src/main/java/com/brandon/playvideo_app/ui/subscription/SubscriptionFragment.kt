package com.brandon.playvideo_app.ui.subscription

import NetworkClient.youtubeApiService
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.brandon.playvideo_app.R
import com.brandon.playvideo_app.data.repository.impl.YoutubeChannelRepositoryImpl
import com.brandon.playvideo_app.data.repository.impl.YoutubeSearchRepositoryImpl
import com.brandon.playvideo_app.data.repository.impl.YoutubeVideoRepositoryImpl
import com.brandon.playvideo_app.databinding.SubscriptionFragmentBinding
import com.brandon.playvideo_app.databinding.ToolbarCommonBinding
import com.brandon.playvideo_app.ui.trend.TrendFragment
import timber.log.Timber

class SubscriptionFragment : Fragment() {

    private var _binding: SubscriptionFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: SubscriptionViewModel by viewModels {
        SubscriptionViewModelFactory(
            youtubeSearchRepository = YoutubeSearchRepositoryImpl(youtubeApiService),
            youtubeChannelRepository = YoutubeChannelRepositoryImpl(youtubeApiService),
            youtubeVideoRepository = YoutubeVideoRepositoryImpl(youtubeApiService),
        )
    }

    companion object {
        @JvmStatic
        fun newInstance() =
            TrendFragment().apply {
                arguments = Bundle().apply {
                    // initial data
                }
            }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View {
        _binding = SubscriptionFragmentBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupToolbar(view)

        viewModel.updateChannels()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupToolbar(view: View) {
        val toolbarBinding = ToolbarCommonBinding.bind(view.findViewById(R.id.included_tool_bar))
        toolbarBinding.toolbarCommon.inflateMenu(R.menu.library_tool_bar_menu)
        toolbarBinding.toolbarCommon.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    Timber.d("Search Item Clicked!")
                    viewModel.channels?.forEach {
                        Timber.tag("Check").d("channel: ${it}")
                    }
                    true
                }

                R.id.setting -> {
                    Timber.d("Setting Item Clicked!")
                    true
                }

                else -> false
            }
        }
    }
}
