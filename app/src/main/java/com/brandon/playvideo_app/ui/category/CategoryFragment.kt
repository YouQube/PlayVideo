package com.brandon.playvideo_app.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.brandon.playvideo_app.R
import com.brandon.playvideo_app.databinding.CategoryFragmentBinding
import com.brandon.playvideo_app.databinding.ToolbarCommonBinding
import com.brandon.playvideo_app.viewmodel.CategoryViewModel
import com.google.android.material.chip.Chip
import timber.log.Timber

class CategoryFragment : Fragment() {
    private var _binding: CategoryFragmentBinding? = null
    private val binding get() = _binding!!
    private val viewModel by viewModels<CategoryViewModel>()
    private val channelAdapter by lazy { ChannelAdapter() }
    private lateinit var categoryVideoAdapter: CategoryVideoAdapter

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

        //진입시 초기 트렌드 비디오 셋팅
        initRecyclerView()
        viewModelObserve()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getTrendingVideos()//초기 화면 트렌드 비디오 셋팅


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
        categoryVideoAdapter = CategoryVideoAdapter(listOf())
        with(binding) {
            //카테고리 영상 recyclerView
            recyclerviewCategory.apply {
                adapter = categoryVideoAdapter
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
            //채널 정보 recyclerView
            recyclerviewChannelsByCategory.apply {
                adapter = channelAdapter
                layoutManager =
                    LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
            }
        }
    }

    //초기 화면 셋팅 칩 생성
    private fun initViews() {
        viewModel.loadingState(false)
        viewModel.getCategoryIds()
        //assignable 허용된 id만 가져와서 칩그룹에 추가하는 코드
        viewModel.categoryIdList.observe(viewLifecycleOwner) { idList ->
            idList.filter { it.snippet.assignable }.forEach { id ->
                binding.chipGroupCategory.addView(createChip(id.snippet.title, id.id))
            }
        }
    }

    private fun createChip(category: String, videoCategoryId: String): Chip {
        return Chip(context).apply {
            setText(category)
            isClickable = true
            isCheckable = true

            setOnClickListener {
                //로딩 ui 처리
                viewModel.loadingState(true)
                //칩이 눌리면 카테고리별 영상과 채널 정보를 가져옴
                viewModel.fetchCategoryVideos(videoCategoryId)
            }
        }
    }

    private fun viewVisible(state: Boolean) {
        binding.tvChannelByCategory.isVisible = state
        binding.constraintLayoutCategoryFragment.isVisible = !state
    }

    private fun viewModelObserve() {
        viewModel.trendVideos.observe(viewLifecycleOwner) {
            categoryVideoAdapter.items = it
            categoryVideoAdapter.notifyDataSetChanged()
        }
        //카테고리 칩이 눌리면 카테고리별 영상과 채널의 썸네일을 보여줌
        viewModel.categoryVideos.observe(viewLifecycleOwner) {
            categoryVideoAdapter.items = it
            categoryVideoAdapter.notifyDataSetChanged()
        }

        viewModel.channel.observe(viewLifecycleOwner) {
            channelAdapter.channelItem = it
            channelAdapter.notifyDataSetChanged()
        }

        viewModel.isLoading.observe(viewLifecycleOwner) {
            binding.pbCategoryLoading.isVisible = it
        }
        //api 통신 상태에 따른 ui 처리
        viewModel.receptionState.observe(viewLifecycleOwner) {
            viewVisible(it)
        }
    }
}

/* 초기 코드
private lateinit var categoryVideoAdapter: CategoryVideoAdapter
private val channelAdapter by lazy { ChannelAdapter() }
private lateinit var channel: List<ChannelItem>
private lateinit var categoryVideos: List<Item>
private lateinit var categories: List<CategoryItem>
private lateinit var trendVideos: List<Item>
private var idList = mutableListOf<String>()

    private fun initRecyclerView() {
        lifecycleScope.launch {
            withContext(Dispatchers.IO) {
                trendVideos = RetrofitInstance.api.getTrendingVideos().items
            }
            categoryVideoAdapter = CategoryVideoAdapter(trendVideos)


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
                binding.pbCategoryLoading.isVisible = true //로딩 처리
                categoryVideos =
                    getCategoryVideos(categoryId) //api 통신

                //channelId를 리스트에 추가
                val channelIdList = mutableListOf<String>()
                categoryVideos.forEach { channelIdList.add(it.snippet.channelId) }
                channel =
                    getChannelInfo(channelIdList) //api 통신

                binding.pbCategoryLoading.isVisible = false //로딩 처리

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
                viewVisible(true, false)//Channels by Category Text-View

                //404에러 API 불러올 수 없음
            }.onFailure {
                receptionFailed()
            }
        }
    }

    //Api 수신 결과에 따른 view 상태
    private fun viewVisible(state: Boolean, loadingState: Boolean) {
        binding.tvChannelByCategory.isVisible = state
        binding.constraintLayoutCategoryFragment.isVisible = !state
        binding.pbCategoryLoading.isVisible = loadingState
    }

    //api 수신 실패시 ui 변경
    private fun receptionFailed() {
        viewVisible(false, false)
        categoryVideoAdapter.items = listOf()
        categoryVideoAdapter.notifyDataSetChanged()

        channelAdapter.channelItem = listOf()
        channelAdapter.notifyDataSetChanged()
    }

    //api 통신 부분
    private suspend fun getCategoryVideos(categoryId: String): List<Item> =
        withContext(Dispatchers.IO) {
            RetrofitInstance.api.getTrendingVideos(videoCategoryId = categoryId).items
        }

    private suspend fun getChannelInfo(channelIdList: MutableList<String>): List<ChannelItem> =
        withContext(Dispatchers.IO) {
            RetrofitInstance.api.getChannelInfo(channelId = channelIdList.joinToString(",")).items
        }
*/

