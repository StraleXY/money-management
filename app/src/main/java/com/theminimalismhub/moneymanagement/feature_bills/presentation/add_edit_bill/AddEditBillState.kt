package com.theminimalismhub.moneymanagement.feature_bills.presentation.add_edit_bill

import androidx.compose.runtime.MutableState
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_bills.domain.model.RecurringType
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category

data class AddEditBillState(
    val currency: String = "",
    val categories: List<Category> = emptyList(),
    val currentBillId: Int? = null,

    val selectedCategoryId: Int? = null,
    val recurringType: RecurringType = RecurringType.MONTHLY,
    val categoryStates: HashMap<Int, MutableState<Boolean>> = HashMap(),

    val selectedAccountId: Int? = null,
    val accounts: List<Account> = emptyList(),
    val accountStates: HashMap<Int, MutableState<Boolean>> = HashMap()

)
