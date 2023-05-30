package com.theminimalismhub.moneymanagement.feature_finances.data.repository

import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_finances.data.data_source.FinanceDao
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.domain.repository.FinanceRepo
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryAmount
import kotlinx.coroutines.flow.Flow

class FinanceRepoImpl constructor(
    private val dao: FinanceDao
) : FinanceRepo {

    override fun getAll(range: Pair<Long, Long>, categoryId: Int?, types: List<FinanceType>, tracked: List<Boolean>): Flow<List<Finance>> {
        if (categoryId == null) return dao.getAll(range.first, range.second, types, tracked)
        return dao.getAll(range.first, range.second, tracked, categoryId)
    }

    override fun getAll(range: Pair<Long, Long>, accountId: Int, types: List<FinanceType>): Flow<List<Finance>> {
        return dao.getAll(range.first, range.second, accountId, types)
    }

    override fun getPerCategory(range: Pair<Long, Long>, type: FinanceType): Flow<List<CategoryAmount>> {
        return dao.getPerCategory(range.first, range.second, type)
    }

    override suspend fun getAmountForTimePeriod(range: Pair<Long, Long>, type: FinanceType, categoryId: Int?): Double {
        if (categoryId == null) return dao.getSpending(range.first, range.second, type)
        else return dao.getSpending(range.first, range.second, categoryId)
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