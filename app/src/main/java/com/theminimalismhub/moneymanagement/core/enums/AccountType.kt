package com.theminimalismhub.moneymanagement.core.enums

enum class AccountType(val value: Int, val title: String) {
    CARD(0, "Card"),
    CASH(1, "Cash"),
    SAVINGS(2, "Savings"),
    HELP(3, "Help"),
    INSURANCE(4, "Insurance");

    companion object {
        private val mapping = FinanceType.values().associateBy(FinanceType::value)
        operator fun get(idx: Int) = mapping[idx]
    }
}