package com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_categories.domain.repository.CategoryRepo

class AddCategory(
    private val repo: CategoryRepo
) {
    suspend operator fun invoke(category: Category): Long {
        return repo.insert(category)
    }
}