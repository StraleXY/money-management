package com.theminimalismhub.moneymanagement.feature_funds.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import com.theminimalismhub.moneymanagement.feature_funds.domain.repository.FundRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetFunds @Inject constructor(
    private val repo: FundRepo
) {
    operator fun invoke() : Flow<List<Fund>> {
        return repo.getAll()
    }
}