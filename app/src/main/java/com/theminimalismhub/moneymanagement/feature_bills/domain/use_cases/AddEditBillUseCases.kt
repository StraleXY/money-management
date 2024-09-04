package com.theminimalismhub.moneymanagement.feature_bills.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases.GetAccounts
import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.GetCategories

data class AddEditBillUseCases(
    val getCategories: GetCategories,
    val getAccounts: GetAccounts,
    val add: AddBill,
    val get: GetBills,
    val delete: DeleteBill
)
