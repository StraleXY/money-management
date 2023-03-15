package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import com.theminimalismhub.moneymanagement.core.enums.AccountType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account

sealed class ManageAccountsEvent {
    data class CardSelected(val idx: Int) : ManageAccountsEvent()
    data class ToggleAddEdit(val account: Account?) : ManageAccountsEvent()
    object ToggleTransaction : ManageAccountsEvent()
    object PrimarySelected : ManageAccountsEvent()
    data class TypeChanged(val type: AccountType) : ManageAccountsEvent()
    object ToggleActive : ManageAccountsEvent()
    class ConfirmTransaction(val accountTo: Account) : ManageAccountsEvent()
    object SaveAccount : ManageAccountsEvent()
    object DeleteAccount : ManageAccountsEvent()
}
