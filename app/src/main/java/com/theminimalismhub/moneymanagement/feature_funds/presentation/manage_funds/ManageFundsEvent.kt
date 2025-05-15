package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund

sealed class ManageFundsEvent {
    object SaveFund : ManageFundsEvent()
    data class ToggleAddEdit(val item: Fund? = null) : ManageFundsEvent()
}