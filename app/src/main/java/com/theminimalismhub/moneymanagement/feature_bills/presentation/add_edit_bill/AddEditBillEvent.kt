package com.theminimalismhub.moneymanagement.feature_bills.presentation.add_edit_bill

import com.theminimalismhub.moneymanagement.feature_bills.domain.model.RecurringType

sealed class AddEditBillEvent {
    data class CategorySelected(val categoryId: Int?) : AddEditBillEvent()
    data class AccountSelected(val accountId: Int?) : AddEditBillEvent()
    data class SelectRecurring(val type: RecurringType) : AddEditBillEvent()
    object AddBill: AddEditBillEvent()
    object DeleteBill: AddEditBillEvent()
}