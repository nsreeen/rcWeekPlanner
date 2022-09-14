package com.example.database

import com.example.models.Event
import com.example.models.EventRow

interface DatabaseInterface {
    suspend fun allEvents(): List<EventRow>
    suspend fun addEvent(userId: Int, summary: String, start: String, end: String,): EventRow?

    suspend fun deleteEvent(id: Int)
}
