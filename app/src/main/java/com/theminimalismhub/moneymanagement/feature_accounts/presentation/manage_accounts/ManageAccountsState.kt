package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.theminimalismhub.moneymanagement.core.enums.AccountType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance

data class ManageAccountsState(
    val currency: String = "RSD",
    val isAddEditOpen: Boolean = false,
    val isTransactionOpen: Boolean = false,
    val accounts: MutableList<Account> = mutableListOf(),
    var selectedAccount: Account? = null,
    val selectedAccountId: Int? = null,
    val currentType: AccountType = AccountType.CASH,
    val accountTypeStates: HashMap<AccountType, MutableState<Boolean>> = hashMapOf(
        AccountType.CASH to mutableStateOf(true),
        AccountType.CARD to mutableStateOf(false),
        AccountType.SAVINGS to mutableStateOf(false),
        AccountType.CRYPTO to mutableStateOf(false)
    ),
    val results: List<Finance> = emptyList()
)
