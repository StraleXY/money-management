package com.theminimalismhub.moneymanagement.feature_finances.domain.use_cases

import com.theminimalismhub.jobmanagerv2.utils.Dater
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo
import java.util.Calendar
import java.util.Date
import javax.inject.Inject

class GetTotal @Inject constructor(
    private val repo: FinanceRepo,
) {
    suspend operator fun invoke(range: Pair<Long, Long>, type: FinanceType, categoryId: Int?, accountId: Int?, tracked: List<Boolean>) : Double {
        return repo.getAmountForTimePeriod(range, type, categoryId, accountId, tracked)
    }
}