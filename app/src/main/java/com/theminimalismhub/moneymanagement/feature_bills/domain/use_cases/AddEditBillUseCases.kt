package com.theminimalismhub.moneymanagement.feature_bills.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.GetAccounts
import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.UpdateAccountBalance
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.GetCategories
import com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases.AddFinance
import javax.inject.Inject

data class AddEditBillUseCases @Inject constructor(
    val getCategories: GetCategories,
    val getAccounts: GetAccounts,
    val add: AddBill,
    val get: GetBills,
    val delete: DeleteBill,
    val pay: AddFinance,
    val updateAccountBalance: UpdateAccountBalance
)
