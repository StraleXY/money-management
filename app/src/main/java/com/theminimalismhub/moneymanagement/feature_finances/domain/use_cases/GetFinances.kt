package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo
import kotlinx.coroutines.flow.Flow

class GetFinances(
    private val repo: FinanceRepo
) {

    operator fun invoke(range: Pair<Long, Long>?) : Flow<List<Finance>> {
        return repo.getAll(range)
    }
}