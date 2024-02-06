package com.brandon.playvideo_app.ui.category

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.brandon.playvideo_app.databinding.CategoryFragmentBinding
import com.google.android.material.chip.Chip
import timber.log.Timber

class CategoryFragment : Fragment() {
    private var _binding: CategoryFragmentBinding? = null
    private val binding get() = _binding!!

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
        Timber.d("Create")
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = CategoryFragmentBinding.inflate(inflater, container, false)
        initViews()
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
    }
    //테스트 코드
    private fun initViews() {
        val category = listOf("a", "b", "c", "d", "e", "f", "g", "h", "i")
        binding.chipGroupCategory.apply {
            category.forEach { category ->
                addView(createChip(category))
            }
        }

    }
    private fun createChip(category: String): Chip {
        return Chip(context).apply {
            setText(category)
            isClickable = true
            isCheckable = true
        }
    }
}