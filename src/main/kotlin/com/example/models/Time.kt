package com.example.models

import java.time.DayOfWeek
import java.time.ZoneId
import java.time.ZoneOffset
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

data class Time(val utcDateTime: String, val dayOfWeek: String)

//ZonedDateTime
//of(int year, int month, int dayOfMonth, int hour, int minute, int second, int nanoOfSecond, ZoneId zone)
fun utcIsoStringFromRcIcsString(s: String): Time {
    //20221031 T101500
    //01234567 89
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
    println(estTime)
    println(utcTime)
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