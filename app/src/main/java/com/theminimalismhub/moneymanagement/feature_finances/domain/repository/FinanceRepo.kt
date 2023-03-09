package com.theminimalismhub.moneymanagement.feature_finances.domain.repository

import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryEarnings
import kotlinx.coroutines.flow.Flow

interface FinanceRepo {
    fun getAll(range: Pair<Long, Long>, categoryId: Int?): Flow<List<Finance>>
    fun getPerCategory(range: Pair<Long, Long>): Flow<List<CategoryEarnings>>
    suspend fun getById(id: Int): Finance?
    suspend fun insert(item: FinanceItem): Long
    suspend fun delete(id: Int)
}