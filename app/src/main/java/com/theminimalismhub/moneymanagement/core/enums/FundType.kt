package com.theminimalismhub.moneymanagement.core.enums

enum class FundType(val value: Int) {
    BUDGET(0),
    RESERVATION(1),
    SAVINGS(2);
    companion object {
        private val mapping = values().associateBy(FundType::value)
        operator fun get(idx: Int) = mapping[idx]
    }
}