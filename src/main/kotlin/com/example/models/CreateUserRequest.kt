package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class CreateUserRequest(
    val id: String,
    val name: String,
    val startTimes: Map<String,String>,
    val endTimes: Map<String,String>,
    val rcToken: String,
)