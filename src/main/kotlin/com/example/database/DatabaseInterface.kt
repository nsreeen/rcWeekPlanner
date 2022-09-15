package com.example.database

import com.example.models.CalendarRow
import com.example.models.EventRow

interface DatabaseInterface {
    suspend fun getEvents(): List<EventRow>
    suspend fun getEvents(calendar_id: Int): List<EventRow>
    suspend fun addEvent(userId: Int, summary: String, start: String, end: String,): EventRow?
    suspend fun deleteEvent(id: Int): Boolean
    suspend fun createCalendar(id: Int, token: String, name: String, online: String, offline: String): CalendarRow?
    suspend fun getCalendar(token: String): CalendarRow?
}
