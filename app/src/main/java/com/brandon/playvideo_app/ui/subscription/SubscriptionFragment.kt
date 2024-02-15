package com.brandon.playvideo_app.ui.subscription

import NetworkClient.youtubeApiService
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.brandon.playvideo_app.R
import com.brandon.playvideo_app.data.repository.impl.YoutubeChannelRepositoryImpl
import com.brandon.playvideo_app.data.repository.impl.YoutubeSearchRepositoryImpl
import com.brandon.playvideo_app.data.repository.impl.YoutubeVideoRepositoryImpl
import com.brandon.playvideo_app.databinding.SubscriptionFragmentBinding
import com.brandon.playvideo_app.databinding.ToolbarCommonBinding
import com.brandon.playvideo_app.ui.detail.video.VideoDetailFragment
import com.brandon.playvideo_app.ui.search.SearchFragment
import com.brandon.playvideo_app.ui.subscription.adapter.HorizontalChannelListAdapter
import com.brandon.playvideo_app.ui.subscription.adapter.VerticalVideoListAdapter
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

    private val horizontalListAdapter: HorizontalChannelListAdapter by lazy {
        HorizontalChannelListAdapter()
    }

    private val verticalVideoListAdapter: VerticalVideoListAdapter by lazy {
        VerticalVideoListAdapter(
            onItemClick = { id ->
                viewModel.onClickItem(id)
            }
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

        initView()
        initListener()
        initViewModel()

    }

    private fun initListener() = with(binding) {
        verticalRecyclerView.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                val layoutManager = recyclerView.layoutManager as LinearLayoutManager
                val visibleItemCount = layoutManager.childCount
                val totalItemCount = layoutManager.itemCount
                val firstVisibleItemPosition = layoutManager.findFirstVisibleItemPosition()

                // findFirstVisibleItemPosition() 는 화면에 아직 아무것도 보이지 않는 상태라면 -1 을 반환. 아래 두 번째 조건은 아이템 로드 전 데이터 로딩을 방지한다.
                if (visibleItemCount + firstVisibleItemPosition >= totalItemCount && firstVisibleItemPosition >= 0) {
                    viewModel.getNextVideos()
                }
            }
        })

        swipeRefreshLayout.setOnRefreshListener {
            viewModel.swipeRefresh()
            binding.swipeRefreshLayout.isRefreshing = false
        }


    }

    private fun initViewModel() = with(viewModel) {
        channelItemHorizontal.observe(viewLifecycleOwner) {
            horizontalListAdapter.submitList(it)
        }

        videosVertical.observe(viewLifecycleOwner) {
            verticalVideoListAdapter.submitList(it)
        }

        val toolbarBinding = ToolbarCommonBinding.bind(requireView().findViewById(R.id.included_tool_bar))
        toolbarBinding.toolbarCommon.inflateMenu(R.menu.common_tool_bar_menu)
        toolbarBinding.toolbarCommon.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    val searchFragment = SearchFragment.newInstance()
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.nav_host_fragment_activity_main, searchFragment)
                        addToBackStack(null)
                        commit()
                    }
                    true
                }

                else -> false
            }
        }

        event.observe(viewLifecycleOwner) { event ->
            when (event) {
                is SubscriptionListEvent.OpenContent -> {
                    Timber.tag("event").d("이벤트 발생!")
                    val detailFragment = VideoDetailFragment.newInstance(event.videoEntity)
                    requireActivity().supportFragmentManager.beginTransaction().apply {
                        replace(R.id.nav_host_fragment_activity_main, detailFragment)
                        addToBackStack(null)
                        commit()
                    }
                }
                is SubscriptionListEvent.LoadErrorState -> {
                    Timber.d("Load error state: ${event.isError}")
                    binding.lottie404.isVisible = event.isError
                    binding.tvAll.isVisible = event.isError.not()
                }
            }
        }
        subscriptionUiState.observe(viewLifecycleOwner) {
            if (it.isInitialLoading.not()) {
                offLoading()
            }
        }
    }

    private fun offLoading() {
        binding.lottieLoading.isVisible = false
        binding.appBarLayout.setExpanded(true, false)
    }

    private fun initView() {
        with(binding.horizontalRecyclerView) {
            adapter = horizontalListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
        }

        with(binding.verticalRecyclerView) {
            adapter = verticalVideoListAdapter
            layoutManager = LinearLayoutManager(requireContext(), LinearLayoutManager.VERTICAL, false)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
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
