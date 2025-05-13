package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds

import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category

data class ManageFundsState(
    val categories: List<Category> = emptyList()
)
