package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.GraphEntry
import java.util.Currency

data class HomeState(
    val isAddEditOpen: Boolean = false,
    val results: List<Finance> = emptyList(),

    val dateRange: Pair<Long, Long> = Pair(System.currentTimeMillis(), System.currentTimeMillis()),
    val isToday: Boolean = true,
    val totalPerCategory: List<CategoryAmount> = emptyList(),
    val categoryBarStates: HashMap<Int, MutableState<CategoryBarState>> = HashMap(),
    val selectedCategoryId: Int? = null,
    val earningsPerTimePeriod: List<GraphEntry> = emptyList(),
    val maxEarnings: Double = 0.0,
    val quickSpendingAmount: Double = 0.0,
    val dailyAverage: Double = 0.0,
    val limit: Double = 0.0,
    val currency: String = "",

    val accounts: List<Account> = emptyList(),
    val accountStates: HashMap<Int, MutableState<Boolean>> = HashMap(),
    val totalsPerAccount: Map<Int, List<CategoryAmount>> = HashMap(),

    val itemsTypeStates: Map<Int, MutableState<Boolean>> = mapOf(
        0 to mutableStateOf(true),
        1 to mutableStateOf(false),
        2 to mutableStateOf(false),
        3 to mutableStateOf(false),
    ),

    val showLineGraph: Boolean = false,
    val collapseCategories: Boolean = false,
    val filterIncomeByAccount: Boolean = false,
    val filterOutcomeByAccount: Boolean = false
)
