package com.theminimalismhub.jobmanagerv2.utils

import java.util.*

class Dater {
    companion object {

        fun getDateRange(month: Int?, year: Int?): Pair<Long, Long> {
            val selectedYear = year ?: getYear()
            var from: Long
            var to: Long
            run {
                val calendar: Calendar = getStartCalendarFor(month, selectedYear)
                from = calendar.timeInMillis
            }
            run {
                val calendar: Calendar = getEndCalendarFor(month, selectedYear)
                to = calendar.timeInMillis
            }
            return Pair(from, to)
        }

        fun getMonth(): Int {
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            return calendar.get(Calendar.MONTH)
        }

        fun getYear(): Int {
            val calendar = Calendar.getInstance()
            calendar.time = Date()
            return calendar.get(Calendar.YEAR)
        }

        private fun getStartCalendarFor(month: Int?, year: Int): Calendar {
            val calendar: Calendar
            if (month == null) {
                calendar = getCalendarFor(year)
                calendar.set(
                    Calendar.MONTH,
                    calendar.getActualMinimum(Calendar.MONTH)
                )
            }
            else calendar = getCalendarFor(month, year)
            calendar.set(
                Calendar.DAY_OF_MONTH,
                calendar.getActualMinimum(Calendar.DAY_OF_MONTH)
            )
            setTimeToBeginningOfDay(calendar)
            return calendar
        }

        private fun getEndCalendarFor(month: Int?, year: Int): Calendar {
            val calendar: Calendar
            if (month == null) {
                calendar = getCalendarFor(year)
                calendar.set(
                    Calendar.MONTH,
                    calendar.getActualMaximum(Calendar.MONTH)
                )
            }
            else calendar = getCalendarFor(month, year)
            calendar.set(
                Calendar.DAY_OF_MONTH,
                calendar.getActualMaximum(Calendar.DAY_OF_MONTH)
            )
            setTimeToEndOfDay(calendar)
            return calendar
        }

        private fun getCalendarFor(month: Int?, year: Int): Calendar {
            if(month == null) return getCalendarFor(year)
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.MONTH, month)
            calendar.set(Calendar.YEAR, year)
            return calendar
        }
        private fun getCalendarFor(year: Int): Calendar {
            val calendar: Calendar = Calendar.getInstance()
            calendar.time = Date()
            calendar.set(Calendar.MONTH, 0)
            calendar.set(Calendar.YEAR, year)
            return calendar
        }

        fun setTimeToBeginningOfDay(calendar: Calendar) {
            calendar.set(Calendar.HOUR_OF_DAY, 0)
            calendar.set(Calendar.MINUTE, 0)
            calendar.set(Calendar.SECOND, 0)
            calendar.set(Calendar.MILLISECOND, 0)
        }

        fun setTimeToEndOfDay(calendar: Calendar) {
            calendar.set(Calendar.HOUR_OF_DAY, 23)
            calendar.set(Calendar.MINUTE, 59)
            calendar.set(Calendar.SECOND, 59)
            calendar.set(Calendar.MILLISECOND, 999)
        }
    }
}