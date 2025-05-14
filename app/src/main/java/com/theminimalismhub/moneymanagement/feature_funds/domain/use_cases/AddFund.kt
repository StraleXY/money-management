package com.theminimalismhub.moneymanagement.feature_funds.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import com.theminimalismhub.moneymanagement.feature_funds.domain.repository.FundRepo
import javax.inject.Inject

class AddFund @Inject constructor(
    private val repo: FundRepo
) {
    suspend operator fun invoke (fund: Fund) : Int {
        return repo.insert(fund.item, fund.categories, fund.accounts, fund.finances)
    }
}