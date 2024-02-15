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
            views < 10000 -> String.format("%.1f천회", views.toDouble() / 1000)
            views < 100000000 -> String.format("%.1f만회", views.toDouble() / 10000)
            else -> String.format("%.1f억회", views.toDouble() / 100000000)
        }
    }

    fun formatSubscriberCount(views: Int?): String {
        views ?: return "알 수 없음"
        return when {
            views < 1000 -> views.toString()
            views < 10000 -> String.format("%.1f천", views.toDouble() / 1000)
            views < 100000000 -> String.format("%.1f만", views.toDouble() / 10000)
            else -> String.format("%.1f억", views.toDouble() / 100000000)
        }
    }

}
