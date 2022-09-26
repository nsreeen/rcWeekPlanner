package com.example.services

import com.example.Modules.*
import com.example.database.Database
import com.example.models.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class EventsService {
    suspend fun getAllEvents(calToken: String, rcToken: String): List<Event> {
        val rcEvents: List<Event> = getRcEvents(rcToken)
        val internalEventRows: List<EventRow> = Database().getEvents(calToken)
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
        return allEvents
    }

    suspend fun getRcEvents(rcToken: String): List<Event> {
        val url: String = "https://www.recurse.com/calendar/events.ics?token=%s".format(rcToken)
        val client = HttpClient(CIO)
        val response: HttpResponse = client.get(url)
        val events = parseIcsEvents(response.bodyAsText())
        val thisWeeksEvents: List<Event> = events.filter { event -> isThisWeek(event.start) }
        return thisWeeksEvents
    }

    suspend fun createEvent(createEventRequest: CreateEventRequest): Event {
        println("creating event")
        val start = standardUtcStringfromLongUtcString(createEventRequest.start)
        val end = standardUtcStringfromLongUtcString(createEventRequest.end)

        val eventRow = Database().addEvent(createEventRequest.calToken, createEventRequest.summary, start, end)
        println(eventRow)
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

    private fun parseIcsEvents(ics: String): List<Event> {
        val icsEvents: List<String> = ics.splitToSequence("BEGIN:VEVENT")
            .filterIndexed { index, _ -> index > 0 }
            .toList()
        return icsEvents
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

    }
}