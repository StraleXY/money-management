package com.theminimalismhub.moneymanagement.feature_accounts.data.repository

import com.theminimalismhub.moneymanagement.feature_accounts.data.data_source.AccountDao
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.domain.repository.AccountRepo
import kotlinx.coroutines.flow.Flow

class AccountRepoImpl constructor(
    private val dao: AccountDao
) : AccountRepo {
    override suspend fun updateAccountBalance(amount: Double, id: Int) {
        dao.updateAccountBalance(amount, id)
    }

    override fun getAll(): Flow<List<Account>> {
        return dao.getAll()
    }

    override suspend fun getById(id: Int): Account? {
        return dao.getAccountById(id)
    }

    override suspend fun insert(item: Account): Long {
        return dao.insertAccount(item)
    }

    override suspend fun delete(id: Int) {
        dao.getAccountById(id)?.let { dao.insertAccount(it.copy(deleted = true)) }
    }
}