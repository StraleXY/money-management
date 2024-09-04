package com.theminimalismhub.moneymanagement.feature_bills.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_bills.domain.repository.BillRepo

class DeleteBill(private val billRepo: BillRepo) {
    suspend operator fun invoke(id: Int) {
        billRepo.delete(id)
    }
}