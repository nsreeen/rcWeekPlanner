package com.example.models

import kotlinx.serialization.Serializable
import java.time.DayOfWeek
@Serializable
data class Event(
    val id: Int,
    val summary: String,
    val start: String,
    val end: String,
    val dayOfWeek: DayOfWeek,
    val isRcEvent: Boolean,
    )