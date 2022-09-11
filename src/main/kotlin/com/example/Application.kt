package com.example

import com.example.models.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import com.example.services.EventsService
import com.example.services.UserService
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
                call.respond(
                    UserService().getUser()
                )
            }
            get("/events") {
                call.respond(
                    EventsService().getRcEvents()
                )
            }
            post("/") {
                val createEventRequest = call.receive<CreateEventRequest>()
                //val event: Event = create_event(createEventRequest)
                call.respond("hello")
            }
        }
    }.start(wait = true)
}


