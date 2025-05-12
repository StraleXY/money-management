package com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases

import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.domain.repository.AccountRepo
import javax.inject.Inject

class AddAccount @Inject constructor(
    private val repo: AccountRepo
) {

    suspend operator fun invoke(account: Account) : Int {
        return repo.insert(account).toInt()
    }
}