package com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_categories.presentation.manage_categories.ManageCategoriesEvent
import javax.inject.Inject

data class ManageCategoriesUseCases @Inject constructor(
    val get: GetCategories,
    val add: AddCategory,
    val delete: DeleteCategory
)
