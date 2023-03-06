package com.theminimalismhub.moneymanagement.core.enums

enum class FinanceType(val value: Int) {
    INCOME(0),
    OUTCOME(1);
    companion object {
        private val mapping = values().associateBy(FinanceType::value)
        operator fun get(idx: Int) = mapping[idx]
    }
}