package com.example.models

import java.time.LocalDateTime
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class Time(val utcDateTime: String, val dayOfWeek: String)

fun utcIsoStringFromRcIcsString(s: String): Time {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
    val ldt: LocalDateTime = LocalDateTime.parse(s, formatter)
    val estTime: ZonedDateTime = ZonedDateTime.of(ldt, ZoneId.of("America/New_York"))
    val utcTime: ZonedDateTime = estTime.withZoneSameInstant(ZoneOffset.UTC)
    return Time(
        utcDateTime=utcTime.toString(),//DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(utcTime),
        dayOfWeek=utcTime.dayOfWeek.name,
    )
}

fun isThisWeek(isoDateTime: String): Boolean {
    val eventDateTime: ZonedDateTime = ZonedDateTime.parse(isoDateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    val currentDateTime: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Etc/UTC"))
    val startOfWeek: ZonedDateTime = currentDateTime.minusDays(currentDateTime.dayOfWeek.ordinal.toLong()).truncatedTo(ChronoUnit.DAYS)
    val startOfEventWeek: ZonedDateTime = eventDateTime.minusDays(eventDateTime.dayOfWeek.ordinal.toLong()).truncatedTo(ChronoUnit.DAYS)
    return startOfEventWeek.withZoneSameInstant(ZoneOffset.UTC) == startOfWeek.withZoneSameInstant(ZoneOffset.UTC)
}