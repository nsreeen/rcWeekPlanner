package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val onlineWindows: Map<String,OnlineWindow>,
    val rcToken: String
    )