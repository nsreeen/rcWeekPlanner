package com.example

import com.example.models.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.events.*


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureHTTP()
        configureSerialization()
        routing {
            get("/") {
                val events: List<Event> = getRcEvents()
                call.respond(events)
            }
            post("/") {
                val createEventRequest = call.receive<CreateEventRequest>()
                val event: Event = create_event(createEventRequest)
                call.respond(event)
            }
        }
    }.start(wait = true)
}

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
            val start: Time = utcIsoStringFromRcIcsString(it.getValue("DTSTART;TZID=America/New_York"))
            val end: Time = utcIsoStringFromRcIcsString(it.getValue("DTEND;TZID=America/New_York"))
            Event(
                summary = it.getValue("SUMMARY"),
                start = start.utcDateTime,
                end = end.utcDateTime,
                dayOfWeek = start.dayOfWeek,
            )
        }
    val thisWeeksEvents: List<Event> = allEvents.filter { event -> isThisWeek(event.start) }
    return thisWeeksEvents
}

//fun create_event(createEventRequent: CreateEventRequest): Event {
//
//}

