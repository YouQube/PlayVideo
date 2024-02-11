package com.brandon.playvideo_app.ui.trend

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
import com.brandon.playvideo_app.databinding.ToolbarCommonBinding
import com.brandon.playvideo_app.databinding.TrendFragmentBinding
import com.brandon.playvideo_app.viewmodel.TrendViewModel
import timber.log.Timber

class TrendFragment : Fragment() {
    private var _binding: TrendFragmentBinding? = null
    private val binding get() = _binding!!

    private val videoAdapter by lazy { VideoAdapter() }
    private val viewModel by viewModels<TrendViewModel>()


    companion object {
        @JvmStatic
        fun newInstance() =
            TrendFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        //Timber.d("Create")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = TrendFragmentBinding.inflate(inflater, container, false)
        initRecyclerView()
        viewModelObserving()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbarBinding = ToolbarCommonBinding.bind(view.findViewById(R.id.included_tool_bar))
        toolbarBinding.toolbarCommon.inflateMenu(R.menu.library_tool_bar_menu)

        toolbarBinding.toolbarCommon.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
                    // 메뉴 아이템 1 클릭 시 동작할 코드 작성
                    Timber.d("Search Item Clicked!")
                    true
                }

                R.id.setting -> {
                    // 메뉴 아이템 2 클릭 시 동작할 코드 작성
                    Timber.d("Setting Item Clicked!")
                    true
                }
                // 다른 메뉴 아이템에 대해서도 필요한 경우 추가할 수 있음
                else -> false
            }
        }
        //viewModel에 데이터 요청
        viewModel.trendingVideos()
        setUpClickListener()
    }

    override fun onDestroy() {
        super.onDestroy()
        _binding = null
    }

    //리사이클러뷰 초기화
    private fun initRecyclerView() {
        with(binding) {
            recyclerView.apply {
                adapter = videoAdapter
                layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
                addOnScrollListener(onScrollListener)
            }
        }
    }


    //viewModel 상태 관찰 //binding 으로 묶고 viewModel 상태를 observing 해도 되는지??
    private fun viewModelObserving() {
        with(binding) {
            viewModel.trendVideos.observe(viewLifecycleOwner) {
                val videos = videoAdapter.currentList.toMutableList().apply {
                    addAll(it)
                }
                videoAdapter.submitList(videos)
            }
            viewModel.isLoading.observe(viewLifecycleOwner) {
                pbTrendLoading.isVisible = it
            }
        }

    }
    private fun setUpClickListener(){
        binding.fbTrendScrollToTop.setOnClickListener {
            binding.recyclerView.smoothScrollToPosition(0)
        }
    }
    private var onScrollListener: RecyclerView.OnScrollListener =
        object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                //스크롤이 끝까지 닫아서 내릴 곳이 없으면 아이템을 추가
                if (!recyclerView.canScrollVertically(1)) {
                    viewModel.loadingState(true)
                    viewModel.getNextTrendingVideos()
                }
                //scrollToTop 버튼 visible
                with(binding) {
                    if (dy < 0 && fbTrendScrollToTop.isVisible) fbTrendScrollToTop.hide()
                    else if (dy > 0 && !fbTrendScrollToTop.isVisible) fbTrendScrollToTop.show()
                    //맨위면 hide
                    if (!recyclerView.canScrollVertically(-1)) fbTrendScrollToTop.hide()
                }
            }
        }
}
