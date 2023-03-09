package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import androidx.compose.runtime.MutableState
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance

data class HomeState(
    val isAddEditOpen: Boolean = false,
    val results: List<Finance> = emptyList(),
    val dateRange: Pair<Long, Long> = Pair(System.currentTimeMillis(), System.currentTimeMillis()),
    val totalPerCategory: List<CategoryEarnings> = emptyList(),
    val categoryBarStates: HashMap<Int, MutableState<CategoryBarState>> = HashMap()
)
