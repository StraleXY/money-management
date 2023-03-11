package com.theminimalismhub.moneymanagement.core.enums

enum class RangeType(val value: Int) {
    DAILY(0),
    WEEKLY(1),
    MONTHLY(2);

    companion object {
        private val mapping = FinanceType.values().associateBy(FinanceType::value)
        operator fun get(idx: Int) = mapping[idx]
    }
}