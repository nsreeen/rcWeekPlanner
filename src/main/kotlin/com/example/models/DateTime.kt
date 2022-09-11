package com.example.models

import io.ktor.http.*
import io.ktor.http.content.*
import io.ktor.serialization.*
import io.ktor.util.reflect.*
import io.ktor.utils.io.*
import io.ktor.utils.io.jvm.javaio.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.nio.charset.Charset
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

class DateTime(zonedDateTime: ZonedDateTime) {
    val zonedDateTime: ZonedDateTime = zonedDateTime;

    companion object DateTimeFactory {
        fun fromRcIscString(s: String): DateTime {
            val formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
            val localDateTime: LocalDateTime = LocalDateTime.parse(s, formatter)
            val estTime: ZonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("America/New_York"))
            val utcTime: ZonedDateTime = estTime.withZoneSameInstant(ZoneOffset.UTC)
            return DateTime(utcTime)
        }
        fun fromUtcString(s: String): DateTime {
            val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mmZ")
            val localDateTime = LocalDateTime.parse(s, formatter)
            val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Etc/UTC"))
            return DateTime(zonedDateTime)
        }
    }

    fun getDayOfWeek(): DayOfWeek {
        return zonedDateTime.dayOfWeek
    }

    fun isThisWeek(): Boolean {
        val currentDateTime: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Etc/UTC"))
        val startOfWeek: ZonedDateTime = currentDateTime.minusDays(currentDateTime.dayOfWeek.ordinal.toLong()).truncatedTo(ChronoUnit.DAYS)
        val startOfEventWeek: ZonedDateTime = zonedDateTime.minusDays(zonedDateTime.dayOfWeek.ordinal.toLong()).truncatedTo(ChronoUnit.DAYS)
        return startOfEventWeek.withZoneSameInstant(ZoneOffset.UTC) == startOfWeek.withZoneSameInstant(ZoneOffset.UTC)
    }

    fun string(): String {
        return zonedDateTime.toString()
    }
}