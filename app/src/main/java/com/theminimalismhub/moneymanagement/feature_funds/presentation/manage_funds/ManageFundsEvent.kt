package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

sealed class ManageFundsEvent {
    object SaveFund : ManageFundsEvent()
}