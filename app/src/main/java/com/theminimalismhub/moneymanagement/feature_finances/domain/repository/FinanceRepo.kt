package com.theminimalismhub.moneymanagement.feature_finances.domain.repository

import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import kotlinx.coroutines.flow.Flow

interface FinanceRepo {
    fun getAll(range: Pair<Long, Long>?): Flow<List<Finance>>
    suspend fun getById(id: Int): Finance?
    suspend fun insert(item: FinanceItem): Long
    suspend fun delete(id: Int)
}