package com.example.services

import com.example.Modules.getTimeWithDayOffset
import com.example.Modules.standardUtcStringfromLongUtcString
import com.example.models.CalendarResponse
import com.example.models.CreateCalendarRequest
import com.example.database.Database


class CalendarService {
    suspend fun getCalendar(calToken: String): CalendarResponse {
        val calendarRow = Database().getCalendar(calToken)
        val events = EventsService().getAllEvents(calToken, calendarRow!!.rcToken)
        return CalendarResponse(
            calToken=calendarRow!!.token,
            name=calendarRow!!.name,
            onlineTime=calendarRow!!.online,
            offlineTime=calendarRow!!.offline,
            events=events,
        )
    }
    suspend fun createCalendar(createCalendarRequest: CreateCalendarRequest): CalendarResponse {
        val online = standardUtcStringfromLongUtcString(createCalendarRequest.online)
        val offline = standardUtcStringfromLongUtcString(createCalendarRequest.offline)

        val token = java.util.UUID.randomUUID().toString()
        val calendarRow = Database().createCalendar(token, createCalendarRequest.name, online, offline, createCalendarRequest.rcToken)
        val rcEvents = EventsService().getRcEvents(createCalendarRequest.rcToken)
        println("rc events: " + rcEvents)
        return CalendarResponse(
            calToken=calendarRow!!.token,
            name=calendarRow!!.name,
            onlineTime=calendarRow!!.online,
            offlineTime=calendarRow!!.offline,
            events=rcEvents,
        )

    }
}