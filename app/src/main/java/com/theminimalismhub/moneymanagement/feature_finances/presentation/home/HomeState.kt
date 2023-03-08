package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance

data class HomeState(
    val isAddEditOpen: Boolean = false,
    val results: List<Finance> = emptyList(),
    val dateRange: Pair<Long, Long> = Pair(System.currentTimeMillis(), System.currentTimeMillis())
)
