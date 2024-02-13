package com.brandon.playvideo_app.util

import java.time.Duration
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId
import java.time.format.DateTimeFormatter

object Converter {
    private fun convertPublishTimeToLocalDateTime(publishTime: String): LocalDateTime? {
        return try {
            val instant = Instant.parse(publishTime)
            LocalDateTime.ofInstant(instant, ZoneId.systemDefault())
        } catch (e: Exception) {
            null
        }
    }

    fun formatPublishedTime(timeString: String?): String {
        timeString ?: return "알 수 없음"
        val dateTime = LocalDateTime.parse(timeString, DateTimeFormatter.ISO_DATE_TIME)
        val now = LocalDateTime.now()
        val duration = Duration.between(dateTime, now).abs()

        return when {
            duration.toMinutes() < 60 -> "${duration.toMinutes()} 분 전"
            duration.toHours() < 24 -> "${duration.toHours()} 시간 전"
            duration.toDays() < 30 -> "${duration.toDays()} 일 전"
            duration.toDays() < 365 -> "${duration.toDays() / 30} 개월 전"
            else -> "${duration.toDays() / 365} 년 전"
        }
    }

    fun formatViews(views: Int?): String {
        views ?: return "알 수 없음"
        return when {
            views < 1000 -> views.toString()
            views < 10000 -> "${views / 1000}천회"
            views < 100000 -> "${views / 10000}만회"
            views < 1000000 -> "${views / 100000}만회"
            views < 10000000 -> "${views / 1000000}백만회"
            views < 100000000 -> "${views / 10000000}천만회"
            else -> "${views / 100000000}억회"
        }
    }

}
