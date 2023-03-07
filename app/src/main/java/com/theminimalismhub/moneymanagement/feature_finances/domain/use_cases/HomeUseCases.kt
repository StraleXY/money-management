package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.GetCategories

data class HomeUseCases(
    val getCategories: GetCategories
)
