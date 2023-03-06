package com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases

import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_categories.domain.repository.CategoryRepo
import kotlinx.coroutines.flow.Flow

class GetCategories(
    private val repo: CategoryRepo
) {
    operator fun invoke() : Flow<List<Category>> {
        return repo.getAll();
    }
}