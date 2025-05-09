package com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.domain.repository.AccountRepo
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAccounts @Inject constructor(
    private val repo: AccountRepo
) {
    operator fun invoke() : Flow<List<Account>> {
        return repo.getAll()
    }
}