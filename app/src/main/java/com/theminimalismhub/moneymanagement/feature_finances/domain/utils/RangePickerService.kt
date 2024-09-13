package com.theminimalismhub.moneymanagement.feature_finances.domain.utils

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
import com.theminimalismhub.jobmanagerv2.utils.Dater
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.core.enums.RangeType
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.YearMonth
import java.util.*

@SuppressLint("SimpleDateFormat")
class RangePickerService {

    private var start: Calendar = Calendar.getInstance()
    private var end: Calendar = Calendar.getInstance()
    private var distance = 1
    var type: RangeType = RangeType.DAILY
    var rangeLength: Int = 1

    init {
        setModeDay()
    }

    fun setModeDay() {
        start.time = Date()
        Dater.setTimeToBeginningOfDay(start)
        end.time = Date()
        Dater.setTimeToEndOfDay(end)
        distance = 1
        rangeLength = 1
        type = RangeType.DAILY
    }

    fun setModeWeek() {
        setModeDay()
        while (start[Calendar.DAY_OF_WEEK] != Calendar.MONDAY) start.add(Calendar.DAY_OF_MONTH, -1)
        while (end[Calendar.DAY_OF_WEEK] != Calendar.SUNDAY) end.add(Calendar.DAY_OF_MONTH, 1)
        distance = 7
        rangeLength = 7
        type = RangeType.WEEKLY
    }

    fun setModeMonth() {
        setModeDay()
        start.set(LocalDateTime.now().year, LocalDateTime.now().monthValue - 1, 1)
        end.set(LocalDateTime.now().year, LocalDateTime.now().monthValue - 1, YearMonth.of(start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1).lengthOfMonth())
        distance = -1
        rangeLength = YearMonth.of(start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1).lengthOfMonth()
        type = RangeType.MONTHLY
    }

    operator fun next() {
        if (distance == -1) {
            start.add(Calendar.MONTH, 1)
            end[start[Calendar.YEAR], start[Calendar.MONTH]] = YearMonth.of(start[Calendar.YEAR], start[Calendar.MONTH] + 1).lengthOfMonth()
            rangeLength = YearMonth.of(start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1).lengthOfMonth()
        } else {
            start.add(Calendar.DAY_OF_MONTH, distance)
            end.add(Calendar.DAY_OF_MONTH, distance)
        }
    }

    fun set(timestamp: Long) {
        start = Calendar.getInstance()
        start.time = Date(timestamp)
        Dater.setTimeToBeginningOfDay(start)

        end = Calendar.getInstance()
        end.time = Date(timestamp)
        Dater.setTimeToEndOfDay(end)
    }

    fun previous() {
        if (distance == -1) {
            start.add(Calendar.MONTH, -1)
            end[start[Calendar.YEAR], start[Calendar.MONTH]] = YearMonth.of(start[Calendar.YEAR], start[Calendar.MONTH] + 1).lengthOfMonth()
            rangeLength = YearMonth.of(start.get(Calendar.YEAR), start.get(Calendar.MONTH) + 1).lengthOfMonth()
        } else {
            start.add(Calendar.DAY_OF_MONTH, -distance)
            end.add(Calendar.DAY_OF_MONTH, -distance)
        }
    }

    fun formattedDate(): String? {
        return when (distance) {
            1 -> SimpleDateFormat("EEEE d MMM").format(start.time)
            7 -> (SimpleDateFormat("MMM d").format(start.time) + " - " + SimpleDateFormat("MMM d").format(end.time)) + "  [Week " + start[Calendar.WEEK_OF_YEAR] + "]"
            else -> SimpleDateFormat("MMMM YYYY").format(start.time)
        }
    }

    fun formatPair(pair: Pair<Long, Long>): String {
        return when (distance) {
            1 -> SimpleDateFormat("EEEE d MMM").format(pair.first)
            7 -> (SimpleDateFormat("MMM d").format(pair.first) + " - " + SimpleDateFormat("MMM d").format(pair.second)) + "  [Week " + start[Calendar.WEEK_OF_YEAR] + "]"
            else -> SimpleDateFormat("MMMM YYYY").format(pair.first)
        }
    }

    fun getDistanceInMillis() : Long {
        return (rangeLength * 24 * 60 * 60 * 1000).toLong()
    }

    fun getCurrentPair(): Pair<Long, Long> {
        return Pair(getStartTimestamp(), getEndTimestamp())
    }

    fun getPreviousPair() : Pair<Long, Long> {
        val currentPair = getCurrentPair()
        return Pair(currentPair.first - getDistanceInMillis(), currentPair.second - getDistanceInMillis())
    }

    fun getNextPair() : Pair<Long, Long> {
        val currentPair = getCurrentPair()
        return Pair(currentPair.first + getDistanceInMillis(), currentPair.second + getDistanceInMillis())
    }

    fun getStartTimestamp(): Long {
        return start.timeInMillis
    }

    fun getCurrentTimestamp(): Long {
        val current = Calendar.getInstance()
        current[start[Calendar.YEAR], start[Calendar.MONTH], start[Calendar.DAY_OF_MONTH], LocalDateTime.now().hour] = LocalDateTime.now().minute
        return current.timeInMillis
    }

    fun getEndTimestamp(): Long {
        return end.timeInMillis
    }

    fun getLength(): Int {
        val difference = Math.abs(end.timeInMillis - start.timeInMillis)
        val differenceDates = difference / (24 * 60 * 60 * 1000)
        return differenceDates.toInt() + 1
    }

    fun getStartDay(): Calendar {
        return start
    }

    fun isToday(): Boolean {
        Log.d("TIME", "${start.timeInMillis} <= ${System.currentTimeMillis()} <= ${end.timeInMillis}")
        return start.timeInMillis <= System.currentTimeMillis() && end.timeInMillis >= System.currentTimeMillis()
    }

    fun setToday() {
        if(distance == 1) setModeDay()
        else if(distance == 7) setModeWeek()
        else setModeMonth()
    }
}