package com.theminimalismhub.moneymanagement.feature_finances.data.data_source

import androidx.room.*
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.data.model.RecommendedFinanceItem
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.Finance
import com.theminimalismhub.moneymanagement.feature_finances.domain.model.RecommendedFinance
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryAmount
import kotlinx.coroutines.flow.Flow

@Dao
interface FinanceDao {

    @Transaction
    @Query("SELECT * FROM finance WHERE type != 'TRANSACTION' ORDER BY timestamp ASC")
    fun getAll(): Flow<List<Finance>>

    @Transaction
    @Query("SELECT * FROM finance WHERE timestamp >= :from AND timestamp <= :to AND (financeAccountId = :accountId OR financeAccountIdFrom = :accountId) AND type IN (:types) ORDER BY timestamp ASC")
    fun getAll(from: Long, to: Long, accountId : Int, types: List<FinanceType>): Flow<List<Finance>>

//    @Transaction
//    @Query("SELECT * FROM finance WHERE timestamp >= :from AND timestamp <= :to AND financeAccountId = :accountId AND type IN (:types) ORDER BY timestamp ASC")
//    fun getAll(from: Long, to: Long, accountId : Int, types: List<FinanceType>): Flow<List<Finance>>

    @Transaction
    @Query("SELECT * FROM finance WHERE timestamp >= :from AND timestamp <= :to AND type IN (:types) AND trackable IN (:tracked) ORDER BY timestamp ASC")
    fun getAll(from: Long, to: Long, types: List<FinanceType>, tracked: List<Boolean>): Flow<List<Finance>>

    @Transaction
    @Query("SELECT * FROM finance WHERE financeCategoryId = :categoryId AND timestamp >= :from AND timestamp <= :to AND type != 'TRANSACTION' AND trackable IN (:tracked) ORDER BY timestamp ASC")
    fun getAll(from: Long, to: Long, tracked: List<Boolean>, categoryId: Int): Flow<List<Finance>>

    @Transaction
    @Query("SELECT * FROM finance WHERE financeCategoryId = :categoryId AND timestamp >= :from AND timestamp <= :to AND type != 'TRANSACTION' AND trackable IN (:tracked) AND financeAccountId = :accountId ORDER BY timestamp ASC")
    fun getAll(from: Long, to: Long, tracked: List<Boolean>, categoryId: Int, accountId: Int): Flow<List<Finance>>

    @Query("SELECT Finance.financeCategoryId as categoryId, Finance.financeAccountId as accountId, Category.name as name, Category.color as color, SUM(amount) AS amount, -1 AS count " +
            "FROM Finance INNER JOIN Category ON Finance.financeCategoryId = Category.categoryId " +
            "WHERE timestamp >= :from AND timestamp <= :to AND Finance.type = :type AND Finance.trackable IN (:tracked) " +
            "GROUP BY financeCategoryId")
    fun getPerCategory(from: Long, to: Long, type: FinanceType, tracked: List<Boolean>): Flow<List<CategoryAmount>>

    @Query("SELECT Finance.financeCategoryId as categoryId, Finance.financeAccountId as accountId, Category.name as name, Category.color as color, SUM(amount) AS amount, -1 AS count " +
            "FROM Finance INNER JOIN Category ON Finance.financeCategoryId = Category.categoryId " +
            "WHERE timestamp >= :from AND timestamp <= :to AND Finance.type = :type AND Finance.trackable IN (:tracked) AND Finance.financeAccountId = :accountId " +
            "GROUP BY financeCategoryId")
    fun getPerCategory(from: Long, to: Long, type: FinanceType, accountId: Int, tracked: List<Boolean>): Flow<List<CategoryAmount>>

    @Query("SELECT IFNULL(SUM(amount), 0) FROM Finance " +
            "WHERE type = :type AND timestamp >= :from AND timestamp <= :to AND trackable IN (:tracked) ")
    suspend fun getSpending(from: Long, to: Long, type: FinanceType, tracked: List<Boolean>): Double

    @Query("SELECT IFNULL(SUM(amount), 0) FROM Finance " +
            "WHERE financeCategoryId = :categoryId AND timestamp >= :from AND timestamp <= :to AND type = :type AND trackable IN (:tracked) ")
    suspend fun getSpendingByCategory(from: Long, to: Long, categoryId: Int, type: FinanceType, tracked: List<Boolean>): Double

    @Query("SELECT IFNULL(SUM(amount), 0) FROM Finance " +
            "WHERE financeAccountId = :accountId AND timestamp >= :from AND timestamp <= :to AND type = :type AND trackable IN (:tracked) ")
    suspend fun getSpendingByAccount(from: Long, to: Long, accountId: Int, type: FinanceType, tracked: List<Boolean>): Double


    @Query("SELECT IFNULL(SUM(amount), 0) FROM Finance " +
            "WHERE financeCategoryId = :categoryId AND financeAccountId = :accountId AND timestamp >= :from AND timestamp <= :to AND type = :type AND trackable IN (:tracked) ")
    suspend fun getSpending(from: Long, to: Long, categoryId: Int, accountId: Int, type: FinanceType, tracked: List<Boolean>): Double

    @Transaction
    @Query("SELECT * FROM finance WHERE id = :id")
    suspend fun getFinanceById(id: Int): Finance?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertFinance(finance: FinanceItem): Long

    @Transaction
    @Query("DELETE FROM finance WHERE id = :id")
    suspend fun deleteFinance(id: Int)

    // Recommended Finance Items
    @Transaction
    @Query("SELECT * FROM recommendedfinance WHERE financeItemId == null ORDER BY timestamp ASC")
    fun getAllRecommended(): Flow<List<RecommendedFinance>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertRecommended(recommended: RecommendedFinanceItem): Long

    @Transaction
    @Query("DELETE FROM recommendedfinance WHERE recommendedId = :id")
    suspend fun deleteRecommended(id: Int)
}