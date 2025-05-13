package com.theminimalismhub.moneymanagement.feature_funds.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases.GetCategories
import javax.inject.Inject

data class ManageFundsUseCases @Inject constructor(
    val getCategories: GetCategories
)
