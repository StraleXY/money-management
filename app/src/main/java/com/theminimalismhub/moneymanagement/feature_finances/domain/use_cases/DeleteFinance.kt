package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo
import javax.inject.Inject

class DeleteFinance @Inject constructor(
    private val repo: FinanceRepo
) {
    suspend operator fun invoke(id: Int) {
        repo.delete(id)
    }

    suspend fun recommended(id: Int) {
        repo.delete(id)
    }
}