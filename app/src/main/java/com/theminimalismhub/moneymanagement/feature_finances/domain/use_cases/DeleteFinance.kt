package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo

class DeleteFinance constructor(
    private val repo: FinanceRepo
) {
    suspend operator fun invoke(id: Int) {
        repo.delete(id)
    }
}