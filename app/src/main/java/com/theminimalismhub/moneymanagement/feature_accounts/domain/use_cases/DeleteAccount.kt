package com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_accounts.domain.repository.AccountRepo
import javax.inject.Inject

class DeleteAccount @Inject constructor(
    private val repo: AccountRepo
) {
    suspend operator fun invoke(id: Int) {
        repo.delete(id)
    }
}