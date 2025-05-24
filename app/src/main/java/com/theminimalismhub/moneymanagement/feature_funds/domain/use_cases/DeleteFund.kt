package com.theminimalismhub.moneymanagement.feature_funds.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundItem
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import com.theminimalismhub.moneymanagement.feature_funds.domain.repository.FundRepo
import javax.inject.Inject

class DeleteFund @Inject constructor(
    private val repo: FundRepo
) {
    suspend operator fun invoke (fundId: Int) {
        repo.delete(fundId)
    }
    suspend operator fun invoke (fund: Fund) {
        fund.item.fundId?.let { repo.delete(it) }
    }
    suspend operator fun invoke (fundItem: FundItem) {
        fundItem.fundId?.let { repo.delete(it) }
    }
}