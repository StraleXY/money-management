package com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.GetFinances
import javax.inject.Inject

data class ManageAccountsUseCases @Inject constructor(
    val add: AddAccount,
    val delete: DeleteAccount,
    val setPrimary: SetPrimaryUseCase,
    val getAll: GetAccounts,
    val addTransaction: AddTransaction,
    val getTransactions: GetFinances
)
