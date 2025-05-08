package com.theminimalismhub.moneymanagement.feature_categories.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_categories.domain.repository.CategoryRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class DeleteCategory @Inject constructor(
    private val repo: CategoryRepo
) {
    suspend operator fun invoke(category: Category) {
        return repo.delete(category)
    }
    suspend operator fun invoke(id: Int) {
        return repo.delete(id)
    }
}