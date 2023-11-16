package com.rrc.adev3007.pixel_perfect.the_y_app.helpers

import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

fun convertToRelativeTime(dateString: String): String {
    val dateFormat = SimpleDateFormat("E, dd MMM yyyy HH:mm:ss z", Locale.ENGLISH)
    val date = dateFormat.parse(dateString)

    if (date != null) {
        val currentTime = Date()
        val diff = currentTime.time - date.time
        val seconds = diff / 1000
        val minutes = seconds / 60
        val hours = minutes / 60
        val days = hours / 24
        val weeks = days / 7

        return when {
            weeks >= 1 -> "${weeks.toInt()}w ago"
            days >= 1 -> "${days.toInt()}d ago"
            hours >= 1 -> "${hours.toInt()}h ago"
            minutes >= 1 -> "${minutes.toInt()}m ago"
            else -> "1m ago"
        }
    }

    return ""
}
