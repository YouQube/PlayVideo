package com.brandon.playvideo_app.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.brandon.playvideo_app.R
import com.brandon.playvideo_app.data.api.RetrofitInstance
import com.brandon.playvideo_app.data.model.ChannelItem
import com.brandon.playvideo_app.databinding.CategoryFragmentBinding
import com.brandon.playvideo_app.databinding.ToolbarCommonBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class CategoryFragment : Fragment() {
    private var _binding: CategoryFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryVideoAdapter: CategoryVideoAdapter
    private lateinit var channelAdapter: ChannelAdapter
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
                    Timber.d("Search Item Clicked!")
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
        CoroutineScope(Dispatchers.IO).launch {
            val responseData = RetrofitInstance.api.getTrendingVideos().items
            categoryVideoAdapter = CategoryVideoAdapter(responseData)

            channelAdapter = ChannelAdapter()

            withContext(Dispatchers.Main) {
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
    }

    private fun initViews() {
        binding.chipGroupCategory.removeAllViewsInLayout()
        //카테고리 목록 받아오기
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val categories = RetrofitInstance.api.getCategoryIds().items
                val category = mutableListOf<String>()
                //assignable이 허용된 api Item만 추가
                categories.filter { it.snippet.assignable }.forEach {
                    category.add(it.snippet.title)
                    idList.add(it.id)
                }
                //칩 셋팅
                withContext(Dispatchers.Main) {
                    binding.chipGroupCategory.apply {
                        category.forEach { category ->
                            addView(createChip(category))
                        }
                    }
                }
            }.onFailure {
                //api 수신 실패 시
                withContext(Dispatchers.Main) {
                    categoryVideoAdapter.items = listOf()
                    categoryVideoAdapter.notifyDataSetChanged()

                    channelAdapter.channelItem = listOf()
                    channelAdapter.notifyDataSetChanged()
                }
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
                    //id의 인덱스 받아 오는 부분
                    var idx = (id % 14) - 1
                    if (idx == -1) {
                        idx = 13
                    }
                    val videoCategoryId = idList[idx]
                    fetchCategory(videoCategoryId)
                }.onFailure {
                    Toast.makeText(context, "index 에러", Toast.LENGTH_SHORT).show()
                }
            }
        }
    }

    //화면 초기화시 카테고리 별 영상 불러오기
    private fun fetchCategory(categoryId: String) {

        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val categoryVideos =
                    RetrofitInstance.api.getTrendingVideos(videoCategoryId = categoryId).items


                //channelId를 리스트에 추가
                val channelIdList = mutableListOf<String>()
                categoryVideos.forEach { channelIdList.add(it.snippet.channelId) }

                var channel = listOf<ChannelItem>()
                channel =
                    RetrofitInstance.api.getChannelInfo(channelId = channelIdList.joinToString(",")).items

                withContext(Dispatchers.Main) {
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
                }
                //404에러 API 불러올 수 없음
            }.onFailure {
                withContext(Dispatchers.Main) {
                    categoryVideoAdapter.items = listOf()
                    categoryVideoAdapter.notifyDataSetChanged()

                    channelAdapter.channelItem = listOf()
                    channelAdapter.notifyDataSetChanged()
                }
            }
        }
    }

}
