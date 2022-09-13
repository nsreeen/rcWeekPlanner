package com.example.database

import DatabaseFactory.dbQuery
import com.example.models.EventRow
import com.example.models.EventRows
import org.jetbrains.exposed.sql.selectAll

class Database: DatabaseInterface {
    override suspend fun allEvents(): List<EventRow> {
        return dbQuery {
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
    }
}