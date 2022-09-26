package com.example.services

import com.example.Modules.standardUtcStringfromLongUtcString
import com.example.models.CreateCalendarRequest
import com.example.database.Database
import com.example.models.CalendarResponse
import io.ktor.server.application.*
import org.slf4j.LoggerFactory


class CalendarService {
    suspend fun getCalendar(token: String): CalendarResponse {
        if (token[0] == 'v') {
            return getCalendarByViewOnlyToken(token)
        } else {
            return getCalendarByEditToken(token)
        }
    }

    suspend fun getCalendarByEditToken(token: String): CalendarResponse {
        val calendarRow = Database().getCalendarByEditToken(token)
        val events = EventsService().getAllEvents(calendarRow!!.viewOnlyToken, calendarRow!!.rcToken)
        return CalendarResponse(
            editToken=calendarRow!!.editToken,
            viewOnlyToken=calendarRow!!.viewOnlyToken,
            name=calendarRow!!.name,
            onlineTime=calendarRow!!.online,
            offlineTime=calendarRow!!.offline,
            events=events,
        )
    }
    suspend fun getCalendarByViewOnlyToken(token: String): CalendarResponse {
        val calendarRow = Database().getCalendarByViewOnlyToken(token)
        val events = EventsService().getAllEvents(token, calendarRow!!.rcToken)
        return CalendarResponse(
            editToken=null,
            viewOnlyToken=calendarRow!!.viewOnlyToken,
            name=calendarRow!!.name,
            onlineTime=calendarRow!!.online,
            offlineTime=calendarRow!!.offline,
            events=events,
        )
    }

    suspend fun createCalendar(createCalendarRequest: CreateCalendarRequest): CalendarResponse {
        val online = standardUtcStringfromLongUtcString(createCalendarRequest.online)
        val offline = standardUtcStringfromLongUtcString(createCalendarRequest.offline)

        val editToken = "e".plus(java.util.UUID.randomUUID().toString())
        val viewOnlyToken = "v".plus(java.util.UUID.randomUUID().toString())
        val logger = LoggerFactory.getLogger(Application::class.java)
        logger.info("editToken: $editToken viewOnlyToken: $viewOnlyToken")

        val calendarRow = Database().createCalendar(editToken, viewOnlyToken, createCalendarRequest.name, online, offline, createCalendarRequest.rcToken)
        val rcEvents = EventsService().getRcEvents(createCalendarRequest.rcToken)

        return CalendarResponse(
            editToken=editToken,
            viewOnlyToken=viewOnlyToken,
            name=calendarRow!!.name,
            onlineTime=calendarRow!!.online,
            offlineTime=calendarRow!!.offline,
            events=rcEvents,
        )

    }
}