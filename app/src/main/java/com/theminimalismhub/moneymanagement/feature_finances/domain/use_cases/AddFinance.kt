package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.data.model.RecommendedFinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo
import javax.inject.Inject

class AddFinance @Inject constructor(
    private val repo: FinanceRepo
) {
    suspend operator fun invoke (finance: FinanceItem) : Int {
        return repo.insert(finance).toInt()
    }

    suspend operator fun invoke (recommended: RecommendedFinanceItem) : Int {
        return repo.insertRecommended(recommended).toInt()
    }
}