package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund

data class ManageFundsState(
    val categories: List<Category> = emptyList(),
    val accounts: List<Account> = emptyList(),
    val funds: List<Fund> = emptyList()
)
