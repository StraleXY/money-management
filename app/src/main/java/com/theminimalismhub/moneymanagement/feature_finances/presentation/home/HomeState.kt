package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import androidx.compose.runtime.MutableState
import com.theminimalismhub.moneymanagement.core.enums.AccountType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.GraphEntry

data class HomeState(
    val isAddEditOpen: Boolean = true,
    val results: List<Finance> = emptyList(),

    val dateRange: Pair<Long, Long> = Pair(System.currentTimeMillis(), System.currentTimeMillis()),
    val totalPerCategory: List<CategoryEarnings> = emptyList(),
    val categoryBarStates: HashMap<Int, MutableState<CategoryBarState>> = HashMap(),
    val earningsPerTimePeriod: List<GraphEntry> = emptyList(),
    val maxEarnings: Double = 0.0,

    val accounts: List<Account> = listOf(
        Account(
            accountId = 1,
            name = "Gotovina",
            balance = 4000.0,
            type = AccountType.CASH,
            active = true,
            primary = true
        ),
        Account(
            accountId = 2,
            name = "Kartica",
            balance = 21450.0,
            type = AccountType.CARD,
            description = "3802",
            active = true,
            primary = false
        )
    )
)
