package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryEarnings
import kotlinx.coroutines.flow.Flow

class GetTotalPerCategory constructor(
    private val repo: FinanceRepo
) {
    operator fun invoke(range: Pair<Long, Long>) : Flow<List<CategoryEarnings>> {
        return repo.getPerCategory(range)
    }
}