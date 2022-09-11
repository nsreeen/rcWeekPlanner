package com.example.services

import com.example.Modules.getTimeWithDayOffset
import com.example.models.User


class UserService {
    fun getUser(id: String): User {
        return User(
            id="",
            name="",
            startTimes=buildTimesMap(12),
            endTimes=buildTimesMap(21),
            rcToken="23d411fdde12626f789c977b90cc995d")
    }
    private fun buildTimesMap(hours: Int): Map<String,String> {
        return mapOf(
            "MONDAY" to getTimeWithDayOffset(hours=hours, dayOffset=1),
            "TUESDAY" to getTimeWithDayOffset(hours=hours, dayOffset=2),
            "WEDNESDAY" to getTimeWithDayOffset(hours=hours, dayOffset=3),
            "THURSDAY" to getTimeWithDayOffset(hours=hours, dayOffset=4),
            "FRIDAY" to getTimeWithDayOffset(hours=hours, dayOffset=5)
        )
    }
}