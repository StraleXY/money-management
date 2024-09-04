package com.theminimalismhub.moneymanagement.feature_bills.presentation

import androidx.compose.runtime.MutableState
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_bills.domain.model.Bill

data class ManageBillsState(
    val isAddEditOpen: Boolean = false,
    val billToPay: Bill? = null,
    val bills: List<Bill> = listOf(),
    val currency: String = "",

    val paymentAccountId: Int? = null,
    val accounts: List<Account> = emptyList(),
    val accountStates: HashMap<Int, MutableState<Boolean>> = HashMap()
)
