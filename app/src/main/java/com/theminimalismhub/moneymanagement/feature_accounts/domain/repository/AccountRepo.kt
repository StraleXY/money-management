package com.theminimalismhub.moneymanagement.feature_accounts.domain.repository

import com.theminimalismhub.moneymanagement.core.data.Repo
import com.theminimalismhub.moneymanagement.core.data.RepoBase
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account

interface AccountRepo : RepoBase<Account> {
    suspend fun updateAccountBalance(amount: Double, id: Int)
}