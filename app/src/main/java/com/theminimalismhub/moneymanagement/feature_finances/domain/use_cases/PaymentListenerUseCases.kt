package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.GetAccounts
import javax.inject.Inject

data class PaymentListenerUseCases @Inject constructor(
    val addFinance: AddFinance,
    val getAccounts: GetAccounts
)
