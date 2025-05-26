package com.theminimalismhub.moneymanagement.feature_finances.presentation.add_edit_finance

import androidx.compose.runtime.MutableState
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund

data class AddEditFinanceState(
    val currency: String = "",
    val categories: List<Category> = emptyList(),
    val funds: List<Fund> = emptyList(),
    val currentFinanceId: Int? = null,

    val selectedCategoryId: Int? = null,
    val currentType: FinanceType = FinanceType.OUTCOME,
    val categoryStates: HashMap<Int, MutableState<Boolean>> = HashMap(),
    val timestamp: Long = System.currentTimeMillis(),
    val currentTrackable: Boolean = true,

    val selectedAccountId: Int? = null,
    val accounts: List<Account> = emptyList(),
    val accountStates: HashMap<Int, MutableState<Boolean>> = HashMap()
)
