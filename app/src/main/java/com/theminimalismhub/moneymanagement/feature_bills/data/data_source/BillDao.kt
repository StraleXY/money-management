package com.theminimalismhub.moneymanagement.feature_bills.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.theminimalismhub.moneymanagement.feature_bills.data.model.BillItem
import com.theminimalismhub.moneymanagement.feature_bills.domain.model.Bill
import kotlinx.coroutines.flow.Flow

@Dao
interface BillDao {

    @Transaction
    @Query("SELECT * FROM bill ORDER BY due ASC")
    fun getAll() : Flow<List<Bill>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBill(bill: BillItem): Long

    @Transaction
    @Query("DELETE FROM bill WHERE billId = :id")
    suspend fun deleteBill(id: Int)
}