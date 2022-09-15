package com.example.database

import DatabaseFactory.dbQuery
import com.example.models.CalendarRow
import com.example.models.CalendarRows
import com.example.models.EventRow
import com.example.models.EventRows
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll

class Database: DatabaseInterface {
    override suspend fun getEvents(): List<EventRow> = dbQuery {
            EventRows.selectAll().map { row ->
                EventRow(
                    id=row[EventRows.id],
                    calToken=row[EventRows.calToken],
                    summary=row[EventRows.summary],
                    start=row[EventRows.start],
                    end=row[EventRows.end],

                )
            }
        }

    override suspend fun getEvents(calToken: String): List<EventRow> {
        return EventRows.select { EventRows.calToken eq calToken }
            .map { row ->
                EventRow(
                    id=row[EventRows.id],
                    calToken=row[EventRows.calToken],
                    summary=row[EventRows.summary],
                    start=row[EventRows.start],
                    end=row[EventRows.end],

                    )
                }
    }

    override suspend fun addEvent(
        calToken: String,
        summary: String,
        start: String,
        end: String,
    ): EventRow? = dbQuery {
            val insertStatement = EventRows.insert {
                it[EventRows.calToken] = calToken
                it[EventRows.summary] = summary
                it[EventRows.start] = start
                it[EventRows.end] = end
            }
            insertStatement.resultedValues?.singleOrNull()?.let { row ->
                EventRow(
                    id = row[EventRows.id],
                    calToken=row[EventRows.calToken],
                    summary = row[EventRows.summary],
                    start = row[EventRows.start],
                    end = row[EventRows.end],
                )
            }
    }

    override suspend fun deleteEvent(id: Int): Boolean = dbQuery {
            EventRows.deleteWhere { EventRows.id eq id } > 0
    }


    override suspend fun createCalendar(token: String, name: String, online: String, offline: String, rcToken: String): CalendarRow? = dbQuery {
            val insertStatement = CalendarRows.insert {
                it[CalendarRows.token] = token
                it[CalendarRows.rcToken] = rcToken
                it[CalendarRows.name] = name
                it[CalendarRows.online] = online
                it[CalendarRows.offline] = offline
            }
            insertStatement.resultedValues?.singleOrNull()?.let { row ->
                CalendarRow(
                    id = row[CalendarRows.id],
                    token = row[CalendarRows.token],
                    name = row[CalendarRows.name],
                    online = row[CalendarRows.online],
                    offline = row[CalendarRows.offline],
                    rcToken = row[CalendarRows.rcToken],
                    )
            }
        }


    override suspend fun getCalendar(token: String): CalendarRow? = dbQuery {
        CalendarRows.select { CalendarRows.token eq token }
            .map { row ->
                CalendarRow(
                    id = row[CalendarRows.id],
                    token = row[CalendarRows.token],
                    name = row[CalendarRows.name],
                    online = row[CalendarRows.online],
                    offline = row[CalendarRows.offline],
                    rcToken = row[CalendarRows.rcToken],
                )
            }.firstOrNull()
    }
}