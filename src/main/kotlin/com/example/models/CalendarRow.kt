package com.example.models

import org.jetbrains.exposed.sql.*

data class CalendarRow (
    val id: Int,
    val editToken: String,
    val viewOnlyToken: String,
    val name: String,
    val online: String,
    val offline: String,
    val rcToken: String,
)

object CalendarRows: Table() {
    val id = integer("id").autoIncrement()
    val editToken = varchar("editToken", 1024)
    val viewOnlyToken = varchar("viewOnlyToken", 1024)
    val name = varchar("name", 1024)
    val online = varchar("online", 128)
    val offline = varchar("offline", 128)
    val rcToken = varchar("rcToken", 1024)

    override val primaryKey = PrimaryKey(id)
}
