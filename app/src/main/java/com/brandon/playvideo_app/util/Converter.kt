package com.brandon.playvideo_app.util

import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

object Converter {
    // 함수 호출 전 입력값에 대해 세이프콜을 적용함으로 non-nullable 한 함수로 작성
    private fun convertPublishTimeToLocalDateTime(publishTime: String): LocalDateTime? {
        return try {
            val instant = Instant.parse(publishTime)
            LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        } catch (e: Exception) {
            null
        }
    }
}
