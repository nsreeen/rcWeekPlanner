package com.example.services

import com.example.Modules.getTimeWithDayOffset
import com.example.models.CalendarResponse
import com.example.models.CreateCalendarRequest
import com.example.models.Event
import com.example.models.User


class CalendarService {
    fun getCalendar(calToken: String): CalendarResponse {
        return CalendarResponse(
            calToken="1234",
            name="TestName",
            startTime="1",
            endTime="2",
            events=emptyList<Event>(),
        )
    }
    fun createCalendar(createCalendarRequest: CreateCalendarRequest): CalendarResponse {
        return CalendarResponse(
            calToken="1234",
            name="TestName",
            startTime="1",
            endTime="2",
            events=emptyList<Event>(),
        )
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