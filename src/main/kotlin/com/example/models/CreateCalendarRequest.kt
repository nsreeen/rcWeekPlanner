package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateCalendarRequest(
    val name: String,
    val online: String,
    val offline: String,
    val rcToken: String,
)