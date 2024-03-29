package com.brandon.playvideo_app.ui.search

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.SearchView
import androidx.activity.OnBackPressedCallback
import androidx.core.view.isGone
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.brandon.playvideo_app.R
import com.brandon.playvideo_app.data.api.RetrofitClient.apiService
import com.brandon.playvideo_app.databinding.SearchFragmentBinding
import com.brandon.playvideo_app.model.SearchListItem
import com.brandon.playvideo_app.ui.library.LibraryVideoDetailFragment
import com.brandon.playvideo_app.ui.search.adapter.SearchListAdapter
import com.brandon.playvideo_app.ui.search.adapter.SearchShortsAdapter
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import timber.log.Timber

class SearchFragment : Fragment() {

    private lateinit var binding : SearchFragmentBinding
    private var resListItem = mutableListOf<SearchListItem>()
    private var resShortsItem = mutableListOf<SearchListItem>()
    private val listAdapter by lazy { SearchListAdapter(resListItem) }
    private val shortsAdapter by lazy { SearchShortsAdapter(resShortsItem) }

    private var listVideoIds = mutableListOf<String?>()
    private var shortsVideoIds = mutableListOf<String?>()
    private val apiKey = "AIzaSyBLbVhwL3SB3shkh-dwMJz23OXpw4FSd2A"

    private val pageListShorts: MutableList<String> = mutableListOf()
    private val pageListLong: MutableList<String> = mutableListOf()
    private var pageIdxShorts = 0
    private var pageIdxLong = 0
    companion object {
        @JvmStatic
        fun newInstance() =
            SearchFragment().apply {
                arguments = Bundle().apply {
                }
            }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("Create")

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = SearchFragmentBinding.inflate(inflater,container,false)
        return binding.root
    }

    @SuppressLint("NotifyDataSetChanged")
    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        binding.rvSearchShorts.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.HORIZONTAL, false)
        binding.rvSearchShorts.adapter = shortsAdapter

        binding.rvSearchList.layoutManager =
            LinearLayoutManager(context,LinearLayoutManager.VERTICAL,false)
        binding.rvSearchList.adapter = listAdapter

        //뒤로 가기
        binding.ivBack.setOnClickListener {
            requireActivity().supportFragmentManager.popBackStack()
        }

        binding.etSearchSearching.setOnQueryTextListener(object : SearchView.OnQueryTextListener,
            androidx.appcompat.widget.SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {

                val query = binding.etSearchSearching.query.toString()
                searchList(query)
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                return false
            }
        })

        with(binding) {

            listAdapter.setOnClickListener(onVideoClicked)
            rvSearchList.adapter = listAdapter
        }

        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                // 뒤로가기 버튼을 눌렀을 때 실행할 동작을 여기에 작성합니다.
                // 예를 들어, 현재 프래그먼트를 닫거나 이전 화면으로 이동하는 등의 동작을 수행할 수 있습니다.

                // 프래그먼트를 닫거나 이전 화면으로 이동하는 코드 예시:
                //findNavController().popBackStack() // 이전 프래그먼트로 이동
                // 또는
                requireActivity().supportFragmentManager.popBackStack() // 현재 액티비티의 뒤로가기 버튼 이벤트 호출

                // 기본적으로 뒤로가기 동작이 필요하지 않을 경우, super.handleOnBackPressed() 호출

            }
        })
    }

    private suspend fun getSearchList(query: String)=
        withContext(Dispatchers.IO){
            apiService.searchVideo(
                apiKey = apiKey,
                part = "snippet",
                q = query,
                maxResults =5,
                order= "relevance",
                regionCode ="KR",
                type ="video",
                videoDuration = "any")
        }

    private suspend fun getSearchShorts(query: String) =
        withContext(Dispatchers.IO){
            apiService.searchVideo(
                apiKey = apiKey,
                part = "snippet",
                q = query,
                maxResults =5,
                order= "relevance",
                regionCode ="KR",
                type ="video",
                videoDuration = "short")
        }

    private suspend fun getViewCount(ids : MutableList<String?>)=
        withContext(Dispatchers.IO){
            val conrvertedId = ids.joinToString(",")
            apiService.getViewCountByVideoId(
                apiKey= apiKey,
                part="statistics",
                id= conrvertedId
            )
        }

    private val onVideoClicked = object : SearchListAdapter.OnItemClickListener {
        override fun onClicked(item: SearchListItem) {
            val fragment: Fragment
//            val bundle = Bundle()
//            bundle.putParcelable("searchResult", listAdapter.items)
            fragment = LibraryVideoDetailFragment.newInstance(item)
//            fragment.arguments = bundle

            replaceFragment(fragment, true)
        }
    }

    fun replaceFragment(fragment: Fragment, isTransition: Boolean) {

        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()

        fragmentTransition.replace(R.id.nav_host_fragment_activity_main, fragment)
            .addToBackStack(fragment.javaClass.simpleName)
        fragmentTransition.commit()
    }


    private fun searchList(query : String){
        binding.tvSearchShort.isGone = false
        resShortsItem.clear()
        resListItem.clear()
        lifecycleScope.launch { //서스펜드 메소드를 사용한다면 라이프사이클 스코프 안에서는 순차적으로 시행된다.
            val shortResult = getSearchShorts(query)

            val pageTokenShorts = shortResult?.nextPageToken

            if (pageTokenShorts != null) {
                pageListShorts.add(pageTokenShorts)
            }
            pageIdxShorts++

            if( shortResult?.pageInfo?.totalResults!! > 0 ) {
                for(item in shortResult.items!!){
                    val title = item.snippet?.title
                    val uploader = item.snippet?.channelTitle
                    val url = item.snippet?.thumbnails?.default?.url
                    val description = item.snippet?.description


                    shortsVideoIds.add(item.id?.videoId)
                    resShortsItem.add(SearchListItem(title,uploader,0,url,description,false,"0",null,null))

                }
            }
            val shortResult2 = getViewCount(shortsVideoIds)

            for(i in resShortsItem.indices){
                val viewCount = shortResult2?.items?.get(i)?.statistics?.viewCount
                if (viewCount != null) {
                    resShortsItem[i].viewCount = viewCount
                }
            }

            val result = getSearchList(query)

            val pageTokenLong = shortResult?.nextPageToken

            if (pageTokenLong != null) {
                pageListShorts.add(pageTokenLong)
            }
            pageIdxLong++

            Timber.tag("test").d("result= %s", result)
            if( result?.pageInfo?.totalResults!! > 0 ) {
                for(item in result.items!!){
                    val title = item.snippet?.title
                    val uploader = item.snippet?.channelTitle
                    val url = item.snippet?.thumbnails?.default?.url
                    val channelId = item.snippet?.channelId
                    val description = item.snippet?.description

                    listVideoIds.add(item.id?.videoId)
                    resListItem.add(SearchListItem(title,uploader,0,url!!,description,false,"0",null,channelId))


                }
            }
            val result2 = getViewCount(listVideoIds)

            for(i in resListItem.indices){
                val viewCount = result2?.items?.get(i)?.statistics?.viewCount
                if (viewCount != null) {
                    resListItem[i].viewCount = viewCount
                }
            }

            listAdapter.items = resListItem
            shortsAdapter.items = resShortsItem

            listAdapter.notifyDataSetChanged()
            shortsAdapter.notifyDataSetChanged()

            val imm = requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromWindow(binding.etSearchSearching.windowToken, 0)
        }
    }




}

