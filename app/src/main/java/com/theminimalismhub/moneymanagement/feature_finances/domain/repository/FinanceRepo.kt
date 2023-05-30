package com.theminimalismhub.moneymanagement.feature_finances.domain.repository

import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryAmount
import kotlinx.coroutines.flow.Flow

interface FinanceRepo {
    fun getAll(range: Pair<Long, Long>, categoryId: Int?, types: List<FinanceType>, tracked: List<Boolean>): Flow<List<Finance>>
    fun getAll(range: Pair<Long, Long>, accountId: Int, types: List<FinanceType>): Flow<List<Finance>>
    fun getPerCategory(range: Pair<Long, Long>, type: FinanceType): Flow<List<CategoryAmount>>
    suspend fun getAmountForTimePeriod(range: Pair<Long, Long>, type: FinanceType, categoryId: Int?): Double
    suspend fun getById(id: Int): Finance?
    suspend fun insert(item: FinanceItem): Long
    suspend fun delete(id: Int)
}