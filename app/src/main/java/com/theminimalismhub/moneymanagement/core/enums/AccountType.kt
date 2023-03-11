package com.theminimalismhub.moneymanagement.core.enums

enum class AccountType(val value: Int) {
    CARD(0),
    CASH(1),
    SAVINGS(2),
    HELP(3),
    INSURANCE(4);

    companion object {
        private val mapping = FinanceType.values().associateBy(FinanceType::value)
        operator fun get(idx: Int) = mapping[idx]
    }
}