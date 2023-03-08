package com.theminimalismhub.moneymanagement.feature_finances.data.repository

import com.theminimalismhub.moneymanagement.feature_finances.data.data_source.FinanceDao
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo
import kotlinx.coroutines.flow.Flow

class FinanceRepoImpl constructor(
    private val dao: FinanceDao
) : FinanceRepo {

    override fun getAll(range: Pair<Long, Long>?): Flow<List<Finance>> {
        if (range == null) return dao.getAll()
        return dao.getAll(range.first, range.second)
    }

    override suspend fun getById(id: Int): Finance? {
        return dao.getFinanceById(id)
    }

    override suspend fun insert(item: FinanceItem): Long {
        return dao.insertFinance(item)
    }

    override suspend fun delete(id: Int) {
        dao.deleteFinance(id)
    }
}