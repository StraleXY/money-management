package com.theminimalismhub.moneymanagement.core.utils

import java.time.*
import java.time.temporal.*

fun LocalDateTime.snapToDayStart(): LocalDateTime =
    this.toLocalDate().atStartOfDay()

fun LocalDateTime.snapToDayEnd(): LocalDateTime =
    this.toLocalDate().atTime(LocalTime.MAX)

fun LocalDateTime.snapToWeekStart(): LocalDateTime =
    this.with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY))
        .toLocalDate()
        .atStartOfDay()

fun LocalDateTime.snapToWeekEnd(): LocalDateTime =
    this.with(TemporalAdjusters.nextOrSame(DayOfWeek.SUNDAY))
        .toLocalDate()
        .atTime(LocalTime.MAX)

fun LocalDateTime.snapToMonthStart(): LocalDateTime =
    this.with(TemporalAdjusters.firstDayOfMonth())
        .toLocalDate()
        .atStartOfDay()

fun LocalDateTime.snapToMonthEnd(): LocalDateTime =
    this.with(TemporalAdjusters.lastDayOfMonth())
        .toLocalDate()
        .atTime(LocalTime.MAX)

fun LocalDateTime.snapToYearStart(): LocalDateTime =
    this.with(TemporalAdjusters.firstDayOfYear())
        .toLocalDate()
        .atStartOfDay()

fun LocalDateTime.snapToYearEnd(): LocalDateTime =
    this.with(TemporalAdjusters.lastDayOfYear())
        .toLocalDate()
        .atTime(LocalTime.MAX)

fun LocalDateTime.toMillis(zoneId: ZoneId = ZoneId.systemDefault()): Long =
    this.atZone(zoneId).toInstant().toEpochMilli()

