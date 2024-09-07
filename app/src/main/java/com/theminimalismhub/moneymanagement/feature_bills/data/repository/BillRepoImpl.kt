package com.theminimalismhub.moneymanagement.feature_bills.data.repository

import com.theminimalismhub.moneymanagement.feature_bills.data.data_source.BillDao
import com.theminimalismhub.moneymanagement.feature_bills.data.model.BillItem
import com.theminimalismhub.moneymanagement.feature_bills.domain.model.Bill
import com.theminimalismhub.moneymanagement.feature_bills.domain.repository.BillRepo
import kotlinx.coroutines.flow.Flow

class BillRepoImpl(private val dao: BillDao) : BillRepo {

    override fun getAll(): Flow<List<Bill>> {
        return dao.getAll()
    }

    override suspend fun insert(item: BillItem): Long {
        return dao.insertBill(item)
    }

    override suspend fun delete(id: Int) {
        dao.deleteBill(id)
    }
}