package com.example.models


import org.jetbrains.exposed.sql.*

data class EventRow (
    val id: Int,
    val userId: Int,
    val summary: String,
    val start: String,
    val end: String,
)

object EventRows: Table() {
    val id = integer("id").autoIncrement()
    val userId = integer("userId")
    val summary = varchar("summary", 1024)
    val start = varchar("start", 128)
    val end = varchar("end", 128)

    override val primaryKey = PrimaryKey(id)
}