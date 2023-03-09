package com.theminimalismhub.moneymanagement.feature_finances.data.data_source

import androidx.room.*
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryEarnings
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {

    @Transaction
    @Query("SELECT * FROM finance ORDER BY timestamp ASC")
    fun getAll(): Flow<List<Finance>>

    @Transaction
    @Query("SELECT * FROM finance WHERE timestamp >= :from AND timestamp <= :to ORDER BY timestamp ASC")
    fun getAll(from: Long, to: Long): Flow<List<Finance>>

    @Query("SELECT Finance.financeCategoryId as categoryId, Category.name as name, Category.color as color, SUM(amount) AS amount " +
            "FROM Finance INNER JOIN Category ON Finance.financeCategoryId = Category.categoryId " +
            "WHERE timestamp >= :from AND timestamp <= :to " +
            "GROUP BY financeCategoryId")
    fun getPerCategory(from: Long, to: Long): Flow<List<CategoryEarnings>>

    @Transaction
    @Query("SELECT * FROM finance WHERE id = :id")
    suspend fun getFinanceById(id: Int): Finance?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinance(finance: FinanceItem): Long

    @Transaction
    @Query("DELETE FROM finance WHERE id = :id")
    suspend fun deleteFinance(id: Int)
}