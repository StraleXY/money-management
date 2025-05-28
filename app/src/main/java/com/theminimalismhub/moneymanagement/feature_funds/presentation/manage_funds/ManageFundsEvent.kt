package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

import com.theminimalismhub.moneymanagement.core.enums.FundType
import com.theminimalismhub.moneymanagement.core.enums.RecurringType
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund

sealed class ManageFundsEvent {
    object SaveFund : ManageFundsEvent()
    object DeleteFund : ManageFundsEvent()
    data class ToggleAddEdit(val item: Fund? = null) : ManageFundsEvent()
    data class SelectFundType(val type: FundType) : ManageFundsEvent()
    data class SelectAccounts(val ids: List<Int> = emptyList()) : ManageFundsEvent()
    data class SelectCategories(val ids: List<Int> = emptyList()) : ManageFundsEvent()
    data class SelectRecurring(val recurring: RecurringType? = null) : ManageFundsEvent()
}