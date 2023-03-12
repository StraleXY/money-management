package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account

data class ManageAccountsState(
    val accounts: List<Account> = emptyList(),
    val selectedAccount: Account? = null
)
