package com.theminimalismhub.moneymanagement.feature_bills.domain.repository

import com.theminimalismhub.moneymanagement.feature_bills.data.model.BillItem
import com.theminimalismhub.moneymanagement.feature_bills.domain.model.Bill
import kotlinx.coroutines.flow.Flow

interface BillRepo {
    fun getAll(): Flow<List<Bill>>
    suspend fun insert(item: BillItem) : Long
    suspend fun delete(id: Int)
}