package com.brandon.playvideo_app.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import com.brandon.playvideo_app.data.api.RetrofitInstance
import com.brandon.playvideo_app.databinding.CategoryFragmentBinding
import com.google.android.material.chip.Chip
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class CategoryFragment : Fragment() {
    private var _binding: CategoryFragmentBinding? = null
    private val binding get() = _binding!!
    private lateinit var categoryVideoAdapter: CategoryVideoAdapter
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
        //Timber.d("Create")
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

    }


    private fun initRecyclerView() {
        CoroutineScope(Dispatchers.IO).launch {
            val responseData = RetrofitInstance.api.getTrendingVideos().items
            categoryVideoAdapter = CategoryVideoAdapter(responseData)
            withContext(Dispatchers.Main) {
                binding.recyclerviewCategory.apply {
                    adapter = categoryVideoAdapter
                    layoutManager =
                        LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
                }
            }
        }
    }

    private fun initViews() {
        //카테고리 받아오기
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val categories = RetrofitInstance.api.getCategoryIds().items
                val category = mutableListOf<String>()
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
                withContext(Dispatchers.Main) {
                    categoryVideoAdapter.items = listOf()
                    categoryVideoAdapter.notifyDataSetChanged()
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
                val idx = (id % 31) - 1
                val videoCategoryId = idList[idx]
                fetchCategory(videoCategoryId)
            }
        }
    }

    //카테고리 별 영상 불러오기
    private fun fetchCategory(categoryId: String) {
        CoroutineScope(Dispatchers.IO).launch {
            runCatching {
                val categoryVideos =
                    RetrofitInstance.api.getTrendingVideos(videoCategoryId = categoryId).items
                withContext(Dispatchers.Main) {
                    categoryVideoAdapter.items = categoryVideos
                    categoryVideoAdapter.notifyDataSetChanged()
                }
                //404에러 API 불러올 수 없음
            }.onFailure {
                withContext(Dispatchers.Main) {
                    categoryVideoAdapter.items = listOf()
                    categoryVideoAdapter.notifyDataSetChanged()
                }
            }
        }
    }
}


