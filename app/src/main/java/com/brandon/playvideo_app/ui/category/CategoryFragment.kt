package com.brandon.playvideo_app.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.children
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.brandon.playvideo_app.R
import com.brandon.playvideo_app.databinding.CategoryFragmentBinding
import com.brandon.playvideo_app.databinding.ToolbarCommonBinding
import com.brandon.playvideo_app.viewmodel.CategoryViewModel
import com.google.android.material.chip.Chip

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

        val toolbarBinding = ToolbarCommonBinding.bind(view.findViewById(R.id.included_tool_bar))
        toolbarBinding.toolbarCommon.inflateMenu(R.menu.common_tool_bar_menu)
        toolbarBinding.toolbarCommon.setOnMenuItemClickListener { menuItem ->
            when (menuItem.itemId) {
                R.id.search -> {
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
        with(viewModel) {
            loadingState(false)
            getCategoryIds()
            //assignable 허용된 id만 가져와서 칩그룹에 추가하는 코드
            categoryIdList.observe(viewLifecycleOwner) { idList ->
                idList.filter { it.snippet.assignable }.forEach { id ->
                    binding.chipGroupCategory.addView(createChip(id.snippet.title, id.id))
                }
            }
        }
    }

    private fun createChip(category: String, videoCategoryId: String): Chip {
        return Chip(context).apply {
            setText(category)
            isClickable = true
            isCheckable = true

            setOnClickListener {
                with(viewModel) {
                    //로딩 ui 처리
                    loadingState(true)
                    //칩이 눌리면 카테고리별 영상과 채널 정보를 가져옴
                    fetchCategoryVideos(videoCategoryId)
                    //칩이 눌리면 카테고리명 저장
                    saveCategoryTitle(category)
                }
            }
        }
    }

    private fun viewVisible(state: Boolean) {
        with(binding) {
            tvChannelByCategory.isVisible = state
            constraintLayoutCategoryFragment.isVisible = !state
            ivCategoryLogo.isVisible = state
        }
    }

    private fun viewModelObserve() {
        with(viewModel) {
            //초기 화면 트렌딩 비디오
            trendVideos.observe(viewLifecycleOwner) {
                categoryVideoAdapter.items = it
                categoryVideoAdapter.notifyDataSetChanged()
            }
            //카테고리 칩이 눌리면 카테고리별 영상과 채널의 썸네일을 보여줌
            categoryVideos.observe(viewLifecycleOwner) {
                categoryVideoAdapter.items = it
                categoryVideoAdapter.notifyDataSetChanged()
            }
            //채널 썸네일
            channel.observe(viewLifecycleOwner) {
                channelAdapter.channelItem = it
                channelAdapter.notifyDataSetChanged()
            }
            //로딩 상태 처리
            isLoading.observe(viewLifecycleOwner) {
                binding.pbCategoryLoading.isVisible = it
            }
            //api 통신 상태에 따른 ui 처리
            receptionState.observe(viewLifecycleOwner) {
                viewVisible(it)
            }
            //최초 CategoryFragment 진입 했을 때 처리
            initState.observe(viewLifecycleOwner) { initState ->
                if (initState) {
                    viewModel.fetchTrendingVideos() //초기 화면 트렌드 비디오 셋팅
                    binding.tvChannelByCategory.isVisible = false
                }
            }
            //선택된 칩의 위치를 찾아서 isChecked 상태로 변경
            saveCategoryTitle.observe(viewLifecycleOwner) { categoryTitle ->
                val selectedChip =
                    binding.chipGroupCategory.children.firstOrNull { (it as Chip).text == categoryTitle } as? Chip
                selectedChip?.isChecked = true
            }
        }
    }
}

