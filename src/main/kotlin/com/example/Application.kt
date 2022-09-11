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


fun main() {
    embeddedServer(Netty, port = 8080, host = "0.0.0.0") {
        configureHTTP()
        configureSerialization()
        routing {
            get("/{user_id}") {
                val userId = call.parameters["user_id"]!!
                call.respond(
                    UserService().getUser(userId)
                )
            }
            get("/events/{user_id}") {
                val user_id = call.parameters["user_id"]!!
                val userRcToken: String = UserService().getUserRcToken(user_id)
                call.respond(
                    EventsService().getRcEvents(userRcToken)
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


