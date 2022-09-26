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
    override suspend fun getEvents(calViewOnlyToken: String): List<EventRow> = dbQuery {
        EventRows.select { EventRows.calViewOnlyToken eq calViewOnlyToken }
            .map { row ->
                EventRow(
                    id=row[EventRows.id],
                    calViewOnlyToken=row[EventRows.calViewOnlyToken],
                    summary=row[EventRows.summary],
                    start=row[EventRows.start],
                    end=row[EventRows.end],

                    )
                }
    }

    override suspend fun addEvent(
        calViewOnlyToken: String,
        summary: String,
        start: String,
        end: String,
    ): EventRow? = dbQuery {
            val insertStatement = EventRows.insert {
                it[EventRows.calViewOnlyToken] = calViewOnlyToken
                it[EventRows.summary] = summary
                it[EventRows.start] = start
                it[EventRows.end] = end
            }
            insertStatement.resultedValues?.singleOrNull()?.let { row ->
                EventRow(
                    id = row[EventRows.id],
                    calViewOnlyToken=row[EventRows.calViewOnlyToken],
                    summary = row[EventRows.summary],
                    start = row[EventRows.start],
                    end = row[EventRows.end],
                )
            }
    }

    override suspend fun deleteEvent(id: Int): Boolean = dbQuery {
            EventRows.deleteWhere { EventRows.id eq id } > 0
    }


    override suspend fun createCalendar(editToken: String, viewOnlyToken: String, name: String, online: String, offline: String, rcToken: String): CalendarRow? = dbQuery {
            val insertStatement = CalendarRows.insert {
                it[CalendarRows.editToken] = editToken
                it[CalendarRows.viewOnlyToken] = viewOnlyToken
                it[CalendarRows.rcToken] = rcToken
                it[CalendarRows.name] = name
                it[CalendarRows.online] = online
                it[CalendarRows.offline] = offline
            }
            insertStatement.resultedValues?.singleOrNull()?.let { row ->
                CalendarRow(
                    id = row[CalendarRows.id],
                    editToken = row[CalendarRows.editToken],
                    viewOnlyToken = row[CalendarRows.viewOnlyToken],
                    name = row[CalendarRows.name],
                    online = row[CalendarRows.online],
                    offline = row[CalendarRows.offline],
                    rcToken = row[CalendarRows.rcToken],
                    )
            }
        }


    override suspend fun getCalendarByEditToken(token: String): CalendarRow? = dbQuery {
        CalendarRows.select { CalendarRows.editToken eq token }
            .map { row ->
                CalendarRow(
                    id = row[CalendarRows.id],
                    editToken = row[CalendarRows.editToken],
                    viewOnlyToken = row[CalendarRows.viewOnlyToken],
                    name = row[CalendarRows.name],
                    online = row[CalendarRows.online],
                    offline = row[CalendarRows.offline],
                    rcToken = row[CalendarRows.rcToken],
                )
            }.firstOrNull()
    }

    override suspend fun getCalendarByViewOnlyToken(token: String): CalendarRow? = dbQuery {
        CalendarRows.select { CalendarRows.viewOnlyToken eq token }
            .map { row ->
                CalendarRow(
                    id = row[CalendarRows.id],
                    editToken = row[CalendarRows.editToken],
                    viewOnlyToken = row[CalendarRows.viewOnlyToken],
                    name = row[CalendarRows.name],
                    online = row[CalendarRows.online],
                    offline = row[CalendarRows.offline],
                    rcToken = row[CalendarRows.rcToken],
                )
            }.firstOrNull()
    }
}