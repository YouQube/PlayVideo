package com.brandon.playvideo_app.ui.library.adapter

import android.graphics.drawable.Drawable
import androidx.fragment.app.Fragment
import com.brandon.playvideo_app.ui.library.LibraryFragment

class EditProfileAdapter(fragment: LibraryFragment){

    private val libraryFragment = LibraryFragment()

    fun editInfo(profileImage: Drawable, name: String, description: String) {
        // 프래그먼트 함수 생성
        libraryFragment.updateData(profileImage, name, description)
    }

//    fun getInfo(): List<String> {
//        return libraryFragment.giveData()
//    }

    fun getImageInfo(): Drawable {
        return libraryFragment.giveImageData()
    }

}