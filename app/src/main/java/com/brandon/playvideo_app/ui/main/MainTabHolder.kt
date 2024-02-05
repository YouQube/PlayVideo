package com.brandon.playvideo_app.ui.main

import androidx.annotation.StringRes
import androidx.fragment.app.Fragment

data class MainTabHolder(
    val fragment: Fragment,
    @StringRes val title: Int
)