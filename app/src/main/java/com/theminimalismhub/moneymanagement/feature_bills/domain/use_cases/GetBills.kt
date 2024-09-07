package com.theminimalismhub.moneymanagement.feature_bills.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_bills.domain.model.Bill
import com.theminimalismhub.moneymanagement.feature_bills.domain.repository.BillRepo
import kotlinx.coroutines.flow.Flow

class GetBills(private val repo: BillRepo) {
    operator fun invoke() : Flow<List<Bill>> {
        return repo.getAll()
    }
}