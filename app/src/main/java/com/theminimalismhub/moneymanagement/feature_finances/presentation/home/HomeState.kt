package com.theminimalismhub.moneymanagement.feature_finances.presentation.home

import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category

data class HomeState(
    val isAddEditOpen: Boolean = false,
    val outcomeCategories: List<Category> = emptyList(),
    val incomeCategories: List<Category> = emptyList()
)
