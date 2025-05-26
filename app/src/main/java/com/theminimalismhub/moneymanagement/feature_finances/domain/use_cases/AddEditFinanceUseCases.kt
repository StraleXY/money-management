package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.UpdateAccountBalance
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.GetCategories
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import com.theminimalismhub.moneymanagement.feature_funds.domain.use_cases.AddFund
import com.theminimalismhub.moneymanagement.feature_funds.domain.use_cases.GetFunds
import javax.inject.Inject

data class AddEditFinanceUseCases @Inject constructor(
    val getCategories: GetCategories,
    val add: AddFinance,
    val delete: DeleteFinance,
    val updateAccountBalance: UpdateAccountBalance,
    val addFund: AddFund
)
