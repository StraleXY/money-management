package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo

class AddFinance constructor(
    private val repo: FinanceRepo
) {
    suspend operator fun invoke (finance: FinanceItem) : Int {
        return repo.insert(finance).toInt()
    }
}