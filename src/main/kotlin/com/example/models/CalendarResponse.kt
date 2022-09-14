package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class CalendarResponse(
    val calToken: String,
    val name: String,
    val startTime: String,
    val endTime: String,
    val events: List<Event>,
)