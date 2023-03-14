package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account

sealed class ManageAccountsEvent {
    data class CardSelected(val idx: Int) : ManageAccountsEvent()
    object ToggleActive : ManageAccountsEvent()
    data class ToggleAddEdit(val account: Account?) : ManageAccountsEvent()
}
