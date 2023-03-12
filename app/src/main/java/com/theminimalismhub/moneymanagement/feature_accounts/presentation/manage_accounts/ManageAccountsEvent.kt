package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

sealed class ManageAccountsEvent {
    data class CardSelected(val idx: Int) : ManageAccountsEvent()
    object ToggleActive : ManageAccountsEvent()
}
