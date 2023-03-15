package com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.AddFinance

data class ManageAccountsUseCases(
    val add: AddAccount,
    val delete: DeleteAccount,
    val setPrimary: SetPrimaryUseCase,
    val getAll: GetAccounts,
    val addTransaction: AddTransaction
)
