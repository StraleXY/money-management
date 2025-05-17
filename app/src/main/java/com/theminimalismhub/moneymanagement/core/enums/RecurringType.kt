package com.theminimalismhub.moneymanagement.core.enums

enum class RecurringType(val value: Int) {
    DAILY(0),
    WEEKLY(1),
    MONTHLY(2),
    YEARLY(3),
    UNTIL_SPENT(4);
    companion object {
        private val mapping = values().associateBy(RecurringType::value)
        operator fun get(idx: Int) = mapping[idx]
    }
}