package com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases

data class ManageAccountsUseCases(
    val add: AddAccount,
    val deleteAccount: DeleteAccount,
    val getAccounts: GetAccounts
)
