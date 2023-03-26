package com.theminimalismhub.moneymanagement.feature_finances.data.data_source

import androidx.room.*
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryAmount
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {

    @Transaction
    @Query("SELECT * FROM finance WHERE type != 'TRANSACTION' ORDER BY timestamp ASC")
    fun getAll(): Flow<List<Finance>>
    @Transaction
    @Query("SELECT * FROM finance WHERE timestamp >= :from AND timestamp <= :to AND financeAccountId = :accountId AND type IN (:types) ORDER BY timestamp ASC")
    fun getAll(from: Long, to: Long, accountId : Int, types: List<FinanceType>): Flow<List<Finance>>

    @Transaction
    @Query("SELECT * FROM finance WHERE timestamp >= :from AND timestamp <= :to AND type != 'TRANSACTION' ORDER BY timestamp ASC")
    fun getAll(from: Long, to: Long): Flow<List<Finance>>

    @Transaction
    @Query("SELECT * FROM finance WHERE financeCategoryId = :categoryId AND timestamp >= :from AND timestamp <= :to AND type != 'TRANSACTION' ORDER BY timestamp ASC")
    fun getAll(from: Long, to: Long, categoryId: Int): Flow<List<Finance>>

    @Query("SELECT Finance.financeCategoryId as categoryId, Finance.financeAccountId as accountId, Category.name as name, Category.color as color, SUM(amount) AS amount " +
            "FROM Finance INNER JOIN Category ON Finance.financeCategoryId = Category.categoryId " +
            "WHERE timestamp >= :from AND timestamp <= :to AND Finance.type = :type AND Finance.trackable = 1 " +
            "GROUP BY financeCategoryId")
    fun getPerCategory(from: Long, to: Long, type: FinanceType): Flow<List<CategoryAmount>>

    @Query("SELECT IFNULL(SUM(amount), 0) FROM Finance " +
            "WHERE type = :type AND timestamp >= :from AND timestamp <= :to AND trackable = 1 ")
    suspend fun getSpending(from: Long, to: Long, type: FinanceType): Double

    @Query("SELECT IFNULL(SUM(amount), 0) FROM Finance " +
            "WHERE financeCategoryId = :categoryId AND timestamp >= :from AND timestamp <= :to AND trackable = 1 ")
    suspend fun getSpending(from: Long, to: Long, categoryId: Int): Double

    @Transaction
    @Query("SELECT * FROM finance WHERE id = :id")
    suspend fun getFinanceById(id: Int): Finance?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinance(finance: FinanceItem): Long

    @Transaction
    @Query("DELETE FROM finance WHERE id = :id")
    suspend fun deleteFinance(id: Int)
}