package com.theminimalismhub.moneymanagement.feature_bills.presentation

import com.theminimalismhub.moneymanagement.feature_bills.domain.model.Bill

sealed class ManageBillsEvent {
    data class ToggleAddEdit(val bill: Bill?) : ManageBillsEvent()
    data class TogglePayBill(val bill: Bill?) : ManageBillsEvent()
    data class PaymentAccountSelected(val accountId: Int?) : ManageBillsEvent()
    object PayBill : ManageBillsEvent()
}