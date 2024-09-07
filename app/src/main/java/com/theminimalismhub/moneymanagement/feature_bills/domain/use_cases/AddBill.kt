package com.theminimalismhub.moneymanagement.feature_bills.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_bills.data.model.BillItem
import com.theminimalismhub.moneymanagement.feature_bills.domain.repository.BillRepo

class AddBill(private val repo: BillRepo) {
    suspend operator fun invoke(item: BillItem) : Int {
        return repo.insert(item).toInt()
    }
}