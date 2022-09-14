package com.example.database

import com.example.models.EventRow

interface DatabaseInterface {
    suspend fun getEvents(): List<EventRow>
    suspend fun getEvents(calendar_id: Int): List<EventRow>
    suspend fun addEvent(userId: Int, summary: String, start: String, end: String,): EventRow?
    suspend fun deleteEvent(id: Int)
}
