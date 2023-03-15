package com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases

data class ManageAccountsUseCases(
    val add: AddAccount,
    val delete: DeleteAccount,
    val setPrimary: SetPrimaryUseCase,
    val getAll: GetAccounts
)
