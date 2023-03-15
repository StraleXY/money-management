package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.theminimalismhub.moneymanagement.core.enums.AccountType
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account

data class ManageAccountsState(
    val isAddEditOpen: Boolean = false,
    val accounts: List<Account> = emptyList(),
    val selectedAccount: Account? = null,
    val selectedAccountId: Int? = null,
    val currentType: AccountType = AccountType.CASH,
    val accountTypeStates: HashMap<AccountType, MutableState<Boolean>> = hashMapOf(
        AccountType.CASH to mutableStateOf(true),
        AccountType.CARD to mutableStateOf(false),
        AccountType.SAVINGS to mutableStateOf(false)
    )
)
