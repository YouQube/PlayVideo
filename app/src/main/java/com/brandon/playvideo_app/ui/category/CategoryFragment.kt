package com.brandon.playvideo_app.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import com.brandon.playvideo_app.R
import com.brandon.playvideo_app.data.api.RetrofitInstance
import com.brandon.playvideo_app.data.model.CategoryItem
import com.brandon.playvideo_app.data.model.ChannelItem
import com.brandon.playvideo_app.data.model.Item
import com.brandon.playvideo_app.databinding.CategoryFragmentBinding
import com.brandon.playvideo_app.databinding.ToolbarCommonBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CategoryFragment : Fragment() {
    private var _binding: CategoryFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryVideoAdapter: CategoryVideoAdapter
    private lateinit var channelAdapter: ChannelAdapter
    private lateinit var channel: List<ChannelItem>
    private lateinit var categoryVideos: List<Item>
    private lateinit var categories: List<CategoryItem>
    private lateinit var trendVideos: List<Item>
    private var idList = mutableListOf<String>()

    companion object {
        @JvmStatic
        fun newInstance() =
            CategoryFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CategoryFragmentBinding.inflate(inflater, container, false)
        initViews()
        initRecyclerView()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val toolbarBinding = ToolbarCommonBinding.bind(view.findViewById(R.id.included_tool_bar))
        toolbarBinding.toolbarCommon.inflateMenu(R.menu.library_tool_bar_menu)
        toolbarBinding.toolbarCommon.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
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

    private fun initRecyclerView() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                trendVideos = RetrofitInstance.api.getTrendingVideos().items
            }
            categoryVideoAdapter = CategoryVideoAdapter(trendVideos)
            channelAdapter = ChannelAdapter()

            with(binding) {
                recyclerviewCategory.apply {
                    adapter = categoryVideoAdapter
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)

                    recyclerviewChannelsByCategory.apply {
                        adapter = channelAdapter
                        layoutManager =
                            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                    }
                }
            }

        }
    }

    private fun initViews() {
        binding.tvChannelByCategory.isVisible =
            false //chip이 선택되지 않았을 경우 Channels by Category 안보이게처리
        //카테고리 목록 받아오기
        lifecycleScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    categories = RetrofitInstance.api.getCategoryIds().items
                }
                val category = mutableListOf<String>()
                //assignable이 허용된 api Item만 추가
                categories.filter { it.snippet.assignable }.forEach {
                    category.add(it.snippet.title)
                    idList.add(it.id)
                }
                //칩 셋팅
                binding.chipGroupCategory.apply {
                    category.forEach { category ->
                        addView(createChip(category))
                    }
                }
            }.onFailure {
                //api 수신 실패 시
                receptionFailed()
            }
        }
    }

    //칩 생성
    private fun createChip(category: String): Chip {
        return Chip(context).apply {
            setText(category)
            isClickable = true
            isCheckable = true
            setOnClickListener {
                runCatching {
                    //id의 인덱스 받아 오는 부분 chip 의 개수가 14개(api assignable), id가 1부터 시작
                    var idx = (id % 14) - 1
                    if (idx == -1) {
                        idx = 13
                    }
                    val videoCategoryId = idList[idx]
                    fetchCategory(videoCategoryId)

                }.onFailure {
                    receptionFailed()
                }
            }
        }
    }

    //화면 초기화시 카테고리 별 영상 불러오기
    private fun fetchCategory(categoryId: String) {

        lifecycleScope.launch {
            runCatching {
                withContext(Dispatchers.IO) {
                    categoryVideos =
                        RetrofitInstance.api.getTrendingVideos(videoCategoryId = categoryId).items
                    //channelId를 리스트에 추가
                    val channelIdList = mutableListOf<String>()
                    categoryVideos.forEach { channelIdList.add(it.snippet.channelId) }

                    channel =
                        RetrofitInstance.api.getChannelInfo(channelId = channelIdList.joinToString(",")).items
                }

                //CategoryVideos
                categoryVideoAdapter.items = categoryVideos
                categoryVideoAdapter.notifyDataSetChanged()

                //Channel By Category
                channelAdapter.channelItem = channel
                channelAdapter.notifyDataSetChanged()

                //포지션 위치 초기화
                with(binding) {
                    recyclerviewCategory.scrollToPosition(0)
                    recyclerviewChannelsByCategory.scrollToPosition(0)
                }
                viewVisible(true)//Channels by Category Text-View

                //404에러 API 불러올 수 없음
            }.onFailure {
                receptionFailed()
            }
        }
    }

    //Api 수신 결과에 따른 view 상태
    private fun viewVisible(state: Boolean) {
        binding.tvChannelByCategory.isVisible = state
        binding.constraintLayoutCategoryFragment.isVisible = !state
    }

    //api 수신 실패시 ui 변경
    private fun receptionFailed() {
        viewVisible(false)
        categoryVideoAdapter.items = listOf()
        categoryVideoAdapter.notifyDataSetChanged()

        channelAdapter.channelItem = listOf()
        channelAdapter.notifyDataSetChanged()
    }
}
