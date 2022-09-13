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
        DatabaseFactory.init()
        routing {
            get("/{user_id}") {
                try {
                    val userId = call.parameters["user_id"]!!
                    call.respond(UserService().getUser(userId))
                } catch (exception: Exception) {
                    println(exception)
                }
            }
            post("/") {
                try {
                    val createUserRequest = call.receive<CreateUserRequest>()
                    val user: User = UserService().createUser(createUserRequest)
                    call.respond(user)
                } catch (exception: Exception) {
                    println(exception)
                }
            }
            get("/events") {
                try {
                    val userId = call.request.queryParameters["user_id"]!!
                    call.respond(EventsService().getRcEvents(userId))
                } catch (exception: Exception) {
                    println(exception)
                }
            }
            post("/events") {
                try {
                    val createEventRequest = call.receive<CreateEventRequest>()
                    println(createEventRequest)
                    call.respond(EventsService().createEvent(createEventRequest))
                } catch (exception: Exception) {
                    println(exception)
                }
            }
        }
    }.start(wait = true)
}


