package com.example

import com.example.models.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.services.EventsService
import com.example.services.CalendarService
import io.ktor.server.application.*
import io.ktor.server.request.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import org.slf4j.LoggerFactory


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureHTTP()
        configureSerialization()
        val databaseUrl = System.getenv("DATABASE_URL")
        DatabaseFactory.init(databaseUrl)
        routing {
            get("/") {
                call.respond("hello")
            }
            get("/calendars/{cal_token}") {
                try {
                    val logger = LoggerFactory.getLogger(Application::class.java)
                    logger.info("Hello World")
                    val calToken = call.parameters["cal_token"]!!
                    call.application.environment.log.info("GET /calendars/$calToken")
                    call.respond(CalendarService().getCalendar(calToken))
                } catch (exception: Exception) {
                    println(exception)
                }
            }
            post("/calendars") {
                try {
                    val createCalendarRequest = call.receive<CreateCalendarRequest>()
                    call.application.environment.log.info("POST /calendars $createCalendarRequest")
                    call.respond(CalendarService().createCalendar(createCalendarRequest))
                } catch (exception: Exception) {
                    println(exception)
                }
            }
            post("/events") {
                try {
                    val createEventRequest = call.receive<CreateEventRequest>()
                    call.application.environment.log.info("POST /events $createEventRequest")
                    call.respond(EventsService().createEvent(createEventRequest))
                } catch (exception: Exception) {
                    println(exception)
                }
            }
            delete("/events") {
                try {
                    val deleteEventRequest = call.receive<DeleteEventRequest>()
                    call.application.environment.log.info("DELETE /events $deleteEventRequest")
                    call.respond(EventsService().deleteEvent(deleteEventRequest))
                } catch (exception: Exception) {
                    println(exception)
                }
            }
        }
    }.start(wait = true)
}


