package com.example.plugins

import io.ktor.server.engine.*
import io.ktor.server.netty.*
import com.example.plugins.*
import io.ktor.server.application.*
import io.ktor.server.response.*
import io.ktor.server.routing.*
import kotlinx.serialization.*
import io.ktor.server.plugins.contentnegotiation.*
import io.ktor.serialization.kotlinx.json.*

fun Application.configureSerialization() {
    install(ContentNegotiation) {
        json()
    }
}