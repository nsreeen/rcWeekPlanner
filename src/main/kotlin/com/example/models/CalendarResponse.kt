package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class CalendarResponse(
    val calToken: String,
    val name: String,
    val onlineTime: String,
    val offlineTime: String,
    val events: List<Event>,
)