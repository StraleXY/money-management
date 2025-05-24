package com.theminimalismhub.moneymanagement.feature_funds.domain.repository

import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundItem
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import kotlinx.coroutines.flow.Flow

interface FundRepo {
    fun getAll() : Flow<List<Fund>>
    suspend fun insert(item: FundItem, categories: List<Category> = emptyList(), accounts: List<Account> = emptyList(), finances: List<FinanceItem> = emptyList()) : Int
    suspend fun delete(id: Int)
}