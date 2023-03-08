package com.theminimalismhub.moneymanagement.feature_finances.data.data_source

import androidx.room.*
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {

    @Transaction
    @Query("SELECT * FROM finance ORDER BY timestamp ASC")
    fun getAll(): Flow<List<Finance>>

    @Transaction
    @Query("SELECT * FROM finance WHERE id = :id")
    suspend fun getFinanceById(id: Int): Finance?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinance(finance: FinanceItem): Long

    @Transaction
    @Query("DELETE FROM finance WHERE id = :id")
    suspend fun deleteFinance(id: Int)
}