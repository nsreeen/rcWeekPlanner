package com.example.services

import com.example.Modules.*
import com.example.database.Database
import com.example.models.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class EventsService {
    suspend fun getAllEvents(userId: String): List<Event> {
        val rcEvents: List<Event> = getRcEvents(userId)
        val internalEventRows: List<EventRow> = Database().getEvents()
        println(internalEventRows)
        val internalEvents = internalEventRows.map {row ->
            Event(
                id=row.id,
                summary=row.summary,
                start=row.start,
                end=row.end,
                dayOfWeek=getDayOfWeek(row.start),
                isRcEvent=false,
            )
        }
        val allEvents = rcEvents + internalEvents
        println("\nall events: ")
        for (event in allEvents) {
            println(event)
        }
        return allEvents
    }

    suspend fun getRcEvents(userId: String): List<Event> {
        val userRcToken: String = "1234"//CalendarService().getUserRcToken(userId)
        val allEvents = getAllRcEvents(userRcToken)
        val thisWeeksEvents: List<Event> = allEvents.filter { event -> isThisWeek(event.start) }
        return thisWeeksEvents
    }
    private suspend fun getAllRcEvents(userRcToken: String): List<Event> {
        val url: String = "https://www.recurse.com/calendar/events.ics?token=%s".format(userRcToken)
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get(url)
        val icsEvents: List<String> = response
            .bodyAsText().splitToSequence("BEGIN:VEVENT")
            .filterIndexed { index, _ -> index > 0 }
            .toList()
        val allEvents = icsEvents
            .map {
                it.lines()
                    .map { it.split(":") }
                    .filter { it.size == 2 }
                    .map { it[0] to it[1] }
                    .toMap()
            }
            .filter { it.contains("SUMMARY") && it.contains("DTSTART;TZID=America/New_York") && it.contains("DTEND;TZID=America/New_York") }
            .map { it ->
                val start: String = utcStringFromRcIscString(it.getValue("DTSTART;TZID=America/New_York"))
                val end: String = utcStringFromRcIscString(it.getValue("DTEND;TZID=America/New_York"))
                Event(
                    id = 0,
                    summary = it.getValue("SUMMARY"),
                    start = start,
                    end = end,
                    dayOfWeek = getDayOfWeek(start),
                    isRcEvent = true,
                )
            }
        return allEvents
    }

    suspend fun createEvent(createEventRequest: CreateEventRequest): Event {
        val start = utcStringFromHourAndDay(createEventRequest.start, createEventRequest.day)
        val end = utcStringFromHourAndDay(createEventRequest.end, createEventRequest.day)

        val eventRow = Database().addEvent(createEventRequest.userId, createEventRequest.summary, start, end)

        return Event(
            id=eventRow!!.id,
            summary=eventRow!!.summary,
            start=eventRow!!.start,
            end=eventRow!!.end,
            dayOfWeek=getDayOfWeek(start),
            isRcEvent=false,
        )
    }

    suspend fun deleteEvent(deleteEventRequest: DeleteEventRequest) {
        Database().deleteEvent(deleteEventRequest.id)
    }
}