package com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories.ManageCategoriesEvent

data class ManageCategoriesUseCases(
    val get: GetCategories,
    val add: AddCategory,
    val delete: DeleteCategory
)
