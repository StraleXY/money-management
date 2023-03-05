package com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories

import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category

data class ManageCategoriesState(
    val outcomeCategories: List<Category> = emptyList(),
    val incomeCategories: List<Category> = emptyList(),
    val isAddEditOpen: Boolean = false
)
