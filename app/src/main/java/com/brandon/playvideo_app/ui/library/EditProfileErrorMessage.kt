package com.brandon.playvideo_app.ui.library

import androidx.annotation.StringRes
import com.brandon.playvideo_app.R


enum class EditProfileErrorMessage(
    @StringRes val message: Int,
) {
    EMPTY_NAME(R.string.empty_name_error),
    EMPTY_DESCRIPTION(R.string.empty_description_error),
}