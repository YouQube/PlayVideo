package com.brandon.playvideo_app.ui.subscription

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.brandon.playvideo_app.R
import com.brandon.playvideo_app.databinding.SubscriptionFragmentBinding
import com.brandon.playvideo_app.databinding.ToolbarCommonBinding
import timber.log.Timber

class SubscriptionFragment : Fragment() {

    private var _binding: SubscriptionFragmentBinding? = null
    private val binding get() = _binding!!

    companion object {
        @JvmStatic
        fun newInstance() = SubscriptionFragment().apply {
            arguments = Bundle().apply {}
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Timber.d("Create")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = SubscriptionFragmentBinding.inflate(inflater, container, false)
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


    }


    override fun onDestroy() {
        _binding = null
        super.onDestroy()
    }


}