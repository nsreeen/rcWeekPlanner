package com.example.models

import kotlinx.serialization.Serializable
import java.time.*
import java.time.format.DateTimeFormatter
import java.time.temporal.ChronoUnit

data class Time(val utcDateTime: String, val dayOfWeek: String)

@Serializable
data class OnlineWindow(
    val startUtcDateTime: String,
    val endUtcDateTime: String
)
//
//fun utcIsoStringFromRcIcsString(s: String): UtcDateTimeString {
//    val formatter = DateTimeFormatter.ofPattern("yyyyMMdd'T'HHmmss")
//    val ldt: LocalDateTime = LocalDateTime.parse(s, formatter)
//    val estTime: ZonedDateTime = ZonedDateTime.of(ldt, ZoneId.of("America/New_York"))
//    val utcTime: ZonedDateTime = estTime.withZoneSameInstant(ZoneOffset.UTC)
//    return UtcDateTimeString(s=utcTime.toString())
//}
//
//fun getDayOfWeek(utcDateTimeString: UtcDateTimeString): DayOfWeek {
//    val zonedDateTime = LocalDateTime.parse(utcDateTimeString.s, DateTimeFormatter.ISO_DATE_TIME).atZone(ZoneId.of("UTC"));
//    return zonedDateTime.dayOfWeek
//}
//
//fun isThisWeek(utcString: String): Boolean {
//    val eventDateTime: ZonedDateTime = ZonedDateTime.parse(utcString, DateTimeFormatter.ISO_ZONED_DATE_TIME)
//    val currentDateTime: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Etc/UTC"))
//    val startOfWeek: ZonedDateTime = currentDateTime.minusDays(currentDateTime.dayOfWeek.ordinal.toLong()).truncatedTo(ChronoUnit.DAYS)
//    val startOfEventWeek: ZonedDateTime = eventDateTime.minusDays(eventDateTime.dayOfWeek.ordinal.toLong()).truncatedTo(ChronoUnit.DAYS)
//    return startOfEventWeek.withZoneSameInstant(ZoneOffset.UTC) == startOfWeek.withZoneSameInstant(ZoneOffset.UTC)
//}
fun getOnlineWindowFromDayAndTimes(day: ZonedDateTime, start: Int, end: Int): OnlineWindow {
    return OnlineWindow(
        ZonedDateTime.of(
            day.year,
            day.monthValue,
            day.dayOfMonth,
            start,
            0,
            0,
            0,
            ZoneId.of("Etc/UTC")
        ).toString(),
        ZonedDateTime.of(
            day.year,
            day.monthValue,
            day.dayOfMonth,
            end,
            0,
            0,
            0,
            ZoneId.of("Etc/UTC")
        ).toString())
}
fun getThisWeeksOnlineWindows(): Map<String,OnlineWindow> {
    val currentDateTime: ZonedDateTime = ZonedDateTime.now(ZoneId.of("Etc/UTC"))
    val startOfWeek: ZonedDateTime = currentDateTime.minusDays(currentDateTime.dayOfWeek.ordinal.toLong()).truncatedTo(ChronoUnit.DAYS)
    return mapOf(
        "MONDAY" to getOnlineWindowFromDayAndTimes(startOfWeek.plusDays(1.toLong()), 12, 21),
        "TUESDAY" to getOnlineWindowFromDayAndTimes(startOfWeek.plusDays(2.toLong()), 12, 21),
        "WEDNESDAY" to getOnlineWindowFromDayAndTimes(startOfWeek.plusDays(3.toLong()), 12, 21),
        "THURSDAY" to getOnlineWindowFromDayAndTimes(startOfWeek.plusDays(4.toLong()), 12, 21),
        "FRIDAY" to getOnlineWindowFromDayAndTimes(startOfWeek.plusDays(5.toLong()), 12, 21)
    )
}
