package com.brandon.playvideo_app.util

import com.brandon.playvideo_app.data.model.ErrorResponse
import com.google.gson.Gson
import retrofit2.Response

object RetrofitUtil {

    // 에러 응답을 처리하기 위한 확장 함수
    fun <T> Response<T>.message(): String {
        val errorBodyString = this.errorBody()?.string()
        return errorBodyString?.let { parseErrorBody(it) } ?: "Unknown Error"
    }

    private fun parseErrorBody(errorBodyString: String): String {
        val gson = Gson()
        val errorResponse: ErrorResponse = gson.fromJson(errorBodyString, ErrorResponse::class.java)
        return errorResponse.error.message
    }

}