package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFinances @Inject constructor(
    private val repo: FinanceRepo
) {
    operator fun invoke(range: Pair<Long, Long>, categoryId: Int?, accountId: Int?, types: List<FinanceType>, tracked: List<Boolean>) : Flow<List<Finance>> {
        if(accountId == null) return repo.getAll(range, categoryId, types, tracked)
        return repo.getAll(range, categoryId, accountId, types, tracked)
    }
    operator fun invoke(range: Pair<Long, Long>, accountId: Int, type: FinanceType) : Flow<List<Finance>> {
        return repo.getAll(range, accountId, listOf(type))
    }
    operator fun invoke(range: Pair<Long, Long>, accountId: Int, types: List<FinanceType>) : Flow<List<Finance>> {
        return repo.getAll(range, accountId, types)
    }
}