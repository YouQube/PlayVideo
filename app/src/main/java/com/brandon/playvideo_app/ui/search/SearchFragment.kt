package com.brandon.playvideo_app.ui.search

import android.annotation.SuppressLint
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.brandon.playvideo_app.R
import com.brandon.playvideo_app.data.api.RetrofitClient.apiService
import com.brandon.playvideo_app.ui.search.adapter.SearchListAdapter
import com.brandon.playvideo_app.databinding.SearchFragmentBinding
import com.brandon.playvideo_app.model.SearchListItem
import com.brandon.playvideo_app.ui.detail.VideoDetailFragment
import com.brandon.playvideo_app.ui.library.adapter.LibraryChannelAdapter
import com.brandon.playvideo_app.ui.library.adapter.LibraryVideoAdapter
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
    private val apiKey = "AIzaSyASGZ29yAFmNLR0ArC0TTj1euF8nE5Ppng"
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

        binding.ivSearchSearchBtn.setOnClickListener {
            val query = binding.etSearchSearching.text.toString()
            lifecycleScope.launch { //서스펜드 메소드를 사용한다면 라이프사이클 스코프 안에서는 순차적으로 시행된다.
                val shortResult = getSearchShorts(query)
                if( shortResult?.pageInfo?.totalResults!! > 0 ) {
                    for(item in shortResult.items!!){
                        val title = item.snippet?.title
                        val uploader = item.snippet?.channelTitle
                        val url = item.snippet?.thumbnails?.default?.url

                        shortsVideoIds.add(item.id?.videoId)
                        resShortsItem.add(SearchListItem(title,uploader,0,url,false,"0",null))

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
                Timber.tag("test").d("result= %s", result)
                if( result?.pageInfo?.totalResults!! > 0 ) {
                    for(item in result.items!!){
                        val title = item.snippet?.title
                        val uploader = item.snippet?.channelTitle
                        val url = item.snippet?.thumbnails?.default?.url

                        listVideoIds.add(item.id?.videoId)
                        resListItem.add(SearchListItem(title,uploader,0,url!!,false,"0",null))


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

            }
        }
        with(binding) {

            listAdapter.setOnClickListener(onVideoClicked)
            rvSearchList.adapter = listAdapter
        }
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
            val bundle = Bundle()
//            bundle.putParcelable("searchResult", listAdapter.items)
            fragment = VideoDetailFragment.newInstance()
            fragment.arguments = bundle

            replaceFragment(fragment, true)
        }
    }

    fun replaceFragment(fragment: Fragment, isTransition: Boolean) {

        val fragmentTransition = requireActivity().supportFragmentManager.beginTransaction()

        if (isTransition) {
            fragmentTransition.setCustomAnimations(
                android.R.anim.slide_out_right,
                android.R.anim.slide_in_left
            )
        }
        fragmentTransition.replace(R.id.nav_host_fragment_activity_main, fragment)
            .addToBackStack(fragment.javaClass.simpleName)
        fragmentTransition.commit()
    }
}