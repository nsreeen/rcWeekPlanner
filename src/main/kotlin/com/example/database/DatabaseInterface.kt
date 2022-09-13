package com.example.database

import com.example.models.EventRow

interface DatabaseInterface {
    suspend fun allEvents(): List<EventRow>
}
