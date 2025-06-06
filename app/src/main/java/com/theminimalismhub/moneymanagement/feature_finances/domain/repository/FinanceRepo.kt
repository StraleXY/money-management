package com.theminimalismhub.moneymanagement.feature_finances.domain.repository

import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.data.model.RecommendedFinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.RecommendedFinance
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryAmount
import kotlinx.coroutines.flow.Flow

interface FinanceRepo {
    fun getAll(range: Pair<Long, Long>, categoryId: Int?, types: List<FinanceType>, tracked: List<Boolean>): Flow<List<Finance>>
    fun getAll(range: Pair<Long, Long>, categoryId: Int?, accountId: Int, types: List<FinanceType>, tracked: List<Boolean>): Flow<List<Finance>>
    fun getAll(range: Pair<Long, Long>, accountId: Int, types: List<FinanceType>): Flow<List<Finance>>
    fun getAllRecommended(): Flow<List<RecommendedFinance>>
    fun getPerCategory(range: Pair<Long, Long>, type: FinanceType, accountId: Int?, tracked: List<Boolean>): Flow<List<CategoryAmount>>
    suspend fun getAmountForTimePeriod(range: Pair<Long, Long>, type: FinanceType, categoryId: Int?, accountId: Int?, tracked: List<Boolean>): Double
    suspend fun getById(id: Int): Finance?
    suspend fun insert(item: FinanceItem): Long
    suspend fun insertRecommended(item: RecommendedFinanceItem) : Long
    suspend fun delete(id: Int)
    suspend fun deleteRecommended(id: Int)
}