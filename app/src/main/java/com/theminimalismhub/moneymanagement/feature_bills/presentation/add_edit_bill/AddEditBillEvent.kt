package com.theminimalismhub.moneymanagement.feature_bills.presentation.add_edit_bill

sealed class AddEditBillEvent {
    data class CategorySelected(val categoryId: Int?) : AddEditBillEvent()
    data class AccountSelected(val accountId: Int?) : AddEditBillEvent()
    object AddBill: AddEditBillEvent()
    object DeleteBill: AddEditBillEvent()
}