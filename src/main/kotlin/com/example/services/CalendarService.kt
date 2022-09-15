package com.example.services

import com.example.Modules.getTimeWithDayOffset
import com.example.models.CalendarResponse
import com.example.models.CreateCalendarRequest
import com.example.models.Event
import com.example.database.Database


class CalendarService {
    fun getCalendar(calToken: String): CalendarResponse {
        return CalendarResponse(
            calToken="1234",
            name="TestName",
            onlineTime="1",
            offlineTime="2",
            events=emptyList<Event>(),
        )
    }
    suspend fun createCalendar(createCalendarRequest: CreateCalendarRequest): CalendarResponse {
        val token = java.util.UUID.randomUUID().toString()
        val calendarRow = Database().createCalendar(token, createCalendarRequest.name, createCalendarRequest.online, createCalendarRequest.offline, createCalendarRequest.rcToken)
        val events = EventsService().getRcEvents(createCalendarRequest.rcToken)
        return CalendarResponse(
            calToken=calendarRow!!.token,
            name=calendarRow!!.name,
            onlineTime=calendarRow!!.online,
            offlineTime=calendarRow!!.offline,
            events=events,
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