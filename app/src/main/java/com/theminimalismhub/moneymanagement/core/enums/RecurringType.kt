package com.theminimalismhub.moneymanagement.core.enums

import com.theminimalismhub.moneymanagement.core.utils.snapToDayEnd
import com.theminimalismhub.moneymanagement.core.utils.snapToDayStart
import com.theminimalismhub.moneymanagement.core.utils.snapToMonthEnd
import com.theminimalismhub.moneymanagement.core.utils.snapToMonthStart
import com.theminimalismhub.moneymanagement.core.utils.snapToWeekEnd
import com.theminimalismhub.moneymanagement.core.utils.snapToWeekStart
import com.theminimalismhub.moneymanagement.core.utils.snapToYearEnd
import com.theminimalismhub.moneymanagement.core.utils.toMillis
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

enum class RecurringType(val value: Int, val label: String) {
    DAILY(0, "Daily"),
    WEEKLY(1, "Weekly"),
    MONTHLY(2, "Monthly"),
    YEARLY(3, "Yearly"),
    UNTIL_SPENT(4, "Until Spent");

    companion object {
        private val mapping = values().associateBy(RecurringType::value)
        operator fun get(idx: Int) = mapping[idx]
    }

    fun getTimeSpan() : Pair<Long, Long>? {
        return getTimeSpan(System.currentTimeMillis())
    }

    fun getTimeSpan(currentTime: Long) : Pair<Long, Long>? {
        val reference = LocalDateTime.ofInstant(Instant.ofEpochMilli(currentTime), ZoneId.systemDefault())
        return when (this) {
            DAILY -> Pair(reference.snapToDayStart().toMillis(), reference.snapToDayEnd().toMillis())
            WEEKLY -> Pair(reference.snapToWeekStart().toMillis(), reference.snapToWeekEnd().toMillis())
            MONTHLY -> Pair(reference.snapToMonthStart().toMillis(), reference.snapToMonthEnd().toMillis())
            YEARLY -> Pair(reference.snapToYearEnd().toMillis(), reference.snapToYearEnd().toMillis())
            UNTIL_SPENT -> null
        }
    }
}