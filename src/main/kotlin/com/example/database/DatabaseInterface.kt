package com.example.database

import com.example.models.CalendarRow
import com.example.models.EventRow

interface DatabaseInterface {
    suspend fun getEvents(calToken: String): List<EventRow>
    suspend fun addEvent(calToken: String, summary: String, start: String, end: String,): EventRow?
    suspend fun deleteEvent(id: Int): Boolean
    suspend fun createCalendar(editToken: String, viewOnlyToken: String, name: String, online: String, offline: String, rcToken: String): CalendarRow?
    suspend fun getCalendarByEditToken(token: String): CalendarRow?
    suspend fun getCalendarByViewOnlyToken(token: String): CalendarRow?
}
