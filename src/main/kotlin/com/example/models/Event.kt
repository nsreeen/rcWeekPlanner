package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class Event(val summary: String, val start: String, val end: String, val dayOfWeek: String)