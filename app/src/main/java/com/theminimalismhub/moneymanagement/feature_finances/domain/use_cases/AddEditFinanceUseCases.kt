package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.UpdateAccountBalance
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.GetCategories

data class AddEditFinanceUseCases(
    val getCategories: GetCategories,
    val add: AddFinance,
    val delete: DeleteFinance,
    val updateAccountBalance: UpdateAccountBalance
)
