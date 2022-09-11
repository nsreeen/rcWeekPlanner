package com.example.models

import kotlinx.serialization.Contextual
import java.time.DayOfWeek

data class Event(
    val summary: String,
    val start: DateTime,
    val end: DateTime,
    val dayOfWeek: DayOfWeek
    )