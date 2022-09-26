package com.example.models

import org.jetbrains.exposed.sql.*

data class TokenRow (
    val id: Int,
    val calId: Int,
    val token: String,
    val isEditToken: Boolean,

)

object TokenRows: Table() {
    val id = integer("id").autoIncrement()
    val calId = integer("calId")
    val token = varchar("online", 128)
    val isEditToken = bool("isEditToken")

    override val primaryKey = PrimaryKey(id)
}
