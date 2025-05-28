package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.GetAccounts
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.GetCategories
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.RecommendedFinance
import com.theminimalismhub.moneymanagement.feature_funds.domain.use_cases.GetFunds
import javax.inject.Inject

data class HomeUseCases @Inject constructor(
    val getFinances: GetFinances,
    val deleteFinance: DeleteFinance,
    val getTotalPerDay: GetTotalPerDay,
    val getAccounts: GetAccounts,
    val getTotal: GetTotal,
    val getCategories: GetCategories,
    val getFunds: GetFunds
)
