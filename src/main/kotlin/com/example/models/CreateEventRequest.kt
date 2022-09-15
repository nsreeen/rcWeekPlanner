package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateEventRequest(
    val summary: String,
    val start: String,
    val end: String,
    val calToken: String,
    val day: String,
    )