package com.example.Modules

import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit


    // Handles conversions betweens date time formats.
    // We will pass date times around as strings in the format "yyyy-MM-dd'T'HH:mmZ"
fun utcStringFromRcIscString(s: String): String {
    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
    val localDateTime: LocalDateTime = LocalDateTime.parse(s, formatter)
    val estTime: ZonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("America/New_York"))
    val utcTime: ZonedDateTime = estTime.withZoneSameInstant(ZoneOffset.UTC)
    return utcTime.toString()
}
fun zonedDateTimefromUtcString(s: String): ZonedDateTime {
    val formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd'T'HH:mm'Z'")
    val localDateTime = LocalDateTime.parse(s, formatter)
    val zonedDateTime = ZonedDateTime.of(localDateTime, ZoneId.of("Etc/UTC"))
    return zonedDateTime
}
fun getDayOfWeek(s: String): DayOfWeek {
    return zonedDateTimefromUtcString(s).dayOfWeek
}

fun isThisWeek(s: String): Boolean {
    val zonedDateTime: ZonedDateTime = zonedDateTimefromUtcString(s)
    val currentDateTime: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Etc/UTC"))
    val startOfWeek: ZonedDateTime = currentDateTime.minusDays(currentDateTime.dayOfWeek.ordinal.toLong()).truncatedTo(ChronoUnit.DAYS)
    val startOfEventWeek: ZonedDateTime = zonedDateTime.minusDays(zonedDateTime.dayOfWeek.ordinal.toLong()).truncatedTo(ChronoUnit.DAYS)
    return startOfEventWeek.withZoneSameInstant(ZoneOffset.UTC) == startOfWeek.withZoneSameInstant(ZoneOffset.UTC)
}

fun getTimeWithDayOffset(hours: Int, dayOffset: Int): String {
    val currentDateTime: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Etc/UTC"))
    val startOfWeek: ZonedDateTime = currentDateTime.minusDays(currentDateTime.dayOfWeek.ordinal.toLong()).truncatedTo(
        ChronoUnit.DAYS)
    val day = startOfWeek.plusDays(dayOffset.toLong())
    return ZonedDateTime.of(
        day.year,
        day.monthValue,
        day.dayOfMonth,
        hours,
        0,
        0,
        0,
        ZoneId.of("Etc/UTC")
    ).withZoneSameInstant(ZoneOffset.UTC).toString()
}