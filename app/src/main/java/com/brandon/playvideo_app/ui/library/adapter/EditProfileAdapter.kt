package com.brandon.playvideo_app.ui.library.adapter

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.brandon.playvideo_app.ui.library.LibraryFragment

class EditProfileAdapter(fragmentActivity: FragmentActivity): FragmentStateAdapter(fragmentActivity) {

    private val libraryFragment = LibraryFragment()

    override fun getItemCount(): Int {
        return 1
    }

    override fun createFragment(position: Int): Fragment {
        TODO("Not yet implemented")
    }

    fun editInfo(profileImage: Drawable, name: String, description: String) {
        // 프래그먼트 함수 생성
        libraryFragment.updateData(profileImage, name, description)
    }

    fun getInfo(): List<String> {
        return libraryFragment.giveData()
    }

    fun getImageInfo(): Drawable {
        return libraryFragment.giveImageData()
    }

}