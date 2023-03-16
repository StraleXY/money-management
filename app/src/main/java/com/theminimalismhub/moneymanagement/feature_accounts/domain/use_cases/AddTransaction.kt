package com.theminimalismhub.moneymanagement.feature_accounts.domain.use_cases

import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.domain.repository.AccountRepo
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo

class AddTransaction constructor(
    private val financeRepo: FinanceRepo,
    private val accountRepo: AccountRepo
) {
    suspend operator fun invoke(accountFrom: Account, accountTo: Account, amount: Double) {
        accountRepo.updateAccountBalance(-amount, accountFrom.accountId!!)
        accountRepo.updateAccountBalance(amount, accountTo.accountId!!)
        financeRepo.insert(FinanceItem(
            name = "Transaction from '${accountFrom.name}'",
            amount = amount,
            timestamp = System.currentTimeMillis(),
            type = FinanceType.TRANSACTION,
            financeAccountId = accountFrom.accountId,
            financeAccountIdFrom = accountTo.accountId
        ))
    }
}