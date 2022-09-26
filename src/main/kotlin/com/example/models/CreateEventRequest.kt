package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateEventRequest(
    val summary: String,
    val start: String,
    val end: String,
    val calViewOnlyToken: String,
    val calEditToken: String,
    val color: String,
    )