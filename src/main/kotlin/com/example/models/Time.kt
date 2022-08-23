package com.example.models

import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class Time(val utcDateTime: String, val dayOfWeek: String)

fun utcIsoStringFromRcIcsString(s: String): Time {
    val estTime: ZonedDateTime = getZonedTime(
        year=s.substring(0,4).toInt(),
        month=s.substring(4,6).toInt(),
        dayOfMonth=s.substring(6,8).toInt(),
        hour=s.substring(9,11).toInt(),
        minute=s.substring(11,13).toInt(),
        second=0,
        nanoOfSecond=0,
        zone=ZoneId.of("America/New_York")
    )
    //val utcTime: ZonedDateTime = estTime.withZoneSameInstant(ZoneId.of("Etc/UTC"))
    val utcTime: ZonedDateTime = estTime.withZoneSameInstant(ZoneOffset.UTC)
    return Time(
        utcDateTime=utcTime.toString(),//DateTimeFormatter.ISO_LOCAL_DATE_TIME.format(utcTime),
        dayOfWeek=utcTime.dayOfWeek.name,
    )
}

fun getZonedTime(
    year: Int,
    month: Int,
    dayOfMonth: Int,
    hour: Int,
    minute: Int,
    second: Int,
    nanoOfSecond: Int,
    zone: ZoneId
): ZonedDateTime {
    return ZonedDateTime.of(year,month,dayOfMonth,hour,minute,second,nanoOfSecond,zone,)
}

// utc iso formatted string -> bool is this week of not
// 2022-08-19T20:00Z -> true
fun isThisWeek(isoDateTime: String): Boolean {
    val eventDateTime: ZonedDateTime = ZonedDateTime.parse(isoDateTime, DateTimeFormatter.ISO_ZONED_DATE_TIME)
    val currentDateTime: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Etc/UTC"))
    val startOfWeek: ZonedDateTime = currentDateTime.minusDays(currentDateTime.dayOfWeek.ordinal.toLong()).truncatedTo(ChronoUnit.DAYS)
    val startOfEventWeek: ZonedDateTime = eventDateTime.minusDays(eventDateTime.dayOfWeek.ordinal.toLong()).truncatedTo(ChronoUnit.DAYS)
    return startOfEventWeek.withZoneSameInstant(ZoneOffset.UTC) == startOfWeek.withZoneSameInstant(ZoneOffset.UTC)
}