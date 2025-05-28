package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

import com.theminimalismhub.moneymanagement.core.enums.FundType
import com.theminimalismhub.moneymanagement.core.enums.RecurringType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundItem
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund

data class ManageFundsState(
    val categories: List<Category> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val funds: List<Fund> = emptyList(),
    val focusedFund: Fund? = null,
    val sFundItem: FundItem? = null,
    val sFundType: FundType = FundType.BUDGET,
    val sAccounts: List<Account> = emptyList(),
    val sCategories: List<Category> = emptyList(),
    val sFinances: List<FinanceItem> = emptyList(),
    val sRecurring: RecurringType? = null,
    val isAddEditOpen: Boolean = false
)
