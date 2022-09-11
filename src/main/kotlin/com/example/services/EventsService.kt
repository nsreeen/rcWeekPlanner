package com.example.services

import com.example.models.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*

class EventsService {
    suspend fun getRcEvents(): List<Event> {
        val url: String = "https://www.recurse.com/calendar/events.ics?token=23d411fdde12626f789c977b90cc995d"
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
                val start: String = DateTimeService().utcStringFromRcIscString(it.getValue("DTSTART;TZID=America/New_York"))
                val end: String = DateTimeService().utcStringFromRcIscString(it.getValue("DTEND;TZID=America/New_York"))
                Event(
                    summary = it.getValue("SUMMARY"),
                    start = start,
                    end = end,
                    dayOfWeek = DateTimeService().getDayOfWeek(start),
                )
            }
        val thisWeeksEvents: List<Event> = allEvents.filter { event -> DateTimeService().isThisWeek(event.start) }
        return thisWeeksEvents
    }
}