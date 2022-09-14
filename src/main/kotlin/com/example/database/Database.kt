package com.example.database

import DatabaseFactory.dbQuery
import com.example.models.Event
import com.example.models.EventRow
import com.example.models.EventRows
import com.example.models.EventRows.id
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.selectAll

class Database: DatabaseInterface {
    override suspend fun allEvents(): List<EventRow> = dbQuery {
            EventRows.selectAll().map { row ->
                EventRow(
                    id=row[EventRows.id],
                    userId=row[EventRows.userId],
                    summary=row[EventRows.summary],
                    start=row[EventRows.start],
                    end=row[EventRows.end],

                )
            }
        }

    override suspend fun addEvent(
        userId: Int,
        summary: String,
        start: String,
        end: String,
    ): EventRow? {
        return dbQuery {
            val insertStatement = EventRows.insert {
                it[EventRows.userId] = userId
                it[EventRows.summary] = summary
                it[EventRows.start] = start
                it[EventRows.end] = end
            }
            insertStatement.resultedValues?.singleOrNull()?.let { row ->
                EventRow(
                    id = row[EventRows.id],
                    userId = row[EventRows.userId],
                    summary = row[EventRows.summary],
                    start = row[EventRows.start],
                    end = row[EventRows.end],

                    )
            }
        }
    }
    override suspend fun deleteEvent(id: Int) {
        dbQuery {
            EventRows.deleteWhere { EventRows.id eq id } > 0
        }
    }
}