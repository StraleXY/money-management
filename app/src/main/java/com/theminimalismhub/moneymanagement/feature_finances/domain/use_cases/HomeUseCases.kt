package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.GetAccounts

data class HomeUseCases(
    val getFinances: GetFinances,
    val getTotalPerDay: GetTotalPerDay,
    val getAccounts: GetAccounts,
    val getTotal: GetTotal
)
