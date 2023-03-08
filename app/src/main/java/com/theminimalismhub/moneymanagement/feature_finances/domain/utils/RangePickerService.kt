package com.theminimalismhub.moneymanagement.feature_finances.domain.utils

import android.annotation.SuppressLint
import android.os.Build
import android.util.Log
import androidx.annotation.RequiresApi
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

    init {
        setModeDay()
    }

    fun setModeDay() {
        start.time = Date()
        start.set(Calendar.HOUR, 0)
        start.set(Calendar.MINUTE, 0)
        end.time = Date()
        end.set(Calendar.HOUR, 23)
        end.set(Calendar.MINUTE, 59)
        distance = 1
        type = RangeType.DAILY
    }

    fun setModeWeek() {
        setModeDay()
        while (start[Calendar.DAY_OF_WEEK] != Calendar.MONDAY) start.add(Calendar.DAY_OF_MONTH, -1)
        while (end[Calendar.DAY_OF_WEEK] != Calendar.SUNDAY) end.add(Calendar.DAY_OF_MONTH, 1)
        distance = 7
        type = RangeType.WEEKLY
    }

    fun setModeMonth() {
        setModeDay()
        start[LocalDateTime.now().year, LocalDateTime.now().monthValue - 1] = 1
        end[LocalDateTime.now().year, LocalDateTime.now().monthValue - 1] =
            LocalDateTime.now().month.maxLength()
        distance = -1
        type = RangeType.MONTHLY
    }

    operator fun next() {
        if (distance == -1) {
            start.add(Calendar.MONTH, 1)
            end[start[Calendar.YEAR], start[Calendar.MONTH]] =
                YearMonth.of(start[Calendar.YEAR], start[Calendar.MONTH] + 1).month.maxLength()
        } else {
            start.add(Calendar.DAY_OF_MONTH, distance)
            end.add(Calendar.DAY_OF_MONTH, distance)
        }
    }

    fun set(timestamp: Long) {
        val startCopy = Calendar.getInstance()
        startCopy.timeInMillis = start.timeInMillis
        val endCopy = Calendar.getInstance()
        endCopy.timeInMillis = end.timeInMillis
        val newTime = Calendar.getInstance()
        newTime.timeInMillis = timestamp
        start[newTime[Calendar.YEAR], newTime[Calendar.MONTH], newTime[Calendar.DAY_OF_MONTH], startCopy[Calendar.HOUR]] =
            startCopy[Calendar.MINUTE]
        end[newTime[Calendar.YEAR], newTime[Calendar.MONTH], newTime[Calendar.DAY_OF_MONTH], endCopy[Calendar.HOUR]] =
            endCopy[Calendar.MINUTE]
        // TODO set text
    }

    fun previous() {
        if (distance == -1) {
            start.add(Calendar.MONTH, -1)
            end[start[Calendar.YEAR], start[Calendar.MONTH]] =
                YearMonth.of(start[Calendar.YEAR], start[Calendar.MONTH] + 1).month.maxLength()
        } else {
            start.add(Calendar.DAY_OF_MONTH, -distance)
            end.add(Calendar.DAY_OF_MONTH, -distance)
        }
        // TODO set text
    }

    fun formattedDate(): String? {
        return when (distance) {
            1 -> SimpleDateFormat("EEEE d MMM").format(start.time)
            7 -> (SimpleDateFormat("MMM d").format(start.time) + " - " + SimpleDateFormat("MMM d").format(end.time)) + "  [Week " + start[Calendar.WEEK_OF_YEAR] + "]"
            else -> SimpleDateFormat("MMMM YYYY").format(start.time)
        }
    }

    fun getStartTimestamp(): Long {
        val startMilis = Calendar.getInstance()
        startMilis[start[Calendar.YEAR], start[Calendar.MONTH], start[Calendar.DAY_OF_MONTH], 0] = 0
        return startMilis.timeInMillis
    }

    fun getCurrentTimestamp(): Long {
        val current = Calendar.getInstance()
        current[start[Calendar.YEAR], start[Calendar.MONTH], start[Calendar.DAY_OF_MONTH], LocalDateTime.now().hour] = LocalDateTime.now().minute
        return current.timeInMillis
    }

    fun getEndTimestamp(): Long {
        val endMilis = Calendar.getInstance()
        endMilis[end[Calendar.YEAR], end[Calendar.MONTH], end[Calendar.DAY_OF_MONTH], 24] = 0
        return endMilis.timeInMillis
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