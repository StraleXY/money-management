package com.theminimalismhub.moneymanagement.core.enums

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
}