package com.example.models

import kotlinx.serialization.Serializable

@Serializable
data class User(
    val id: String,
    val name: String,
    val startTimes: Map<String,String>,
    val endTimes: Map<String,String>,
    )