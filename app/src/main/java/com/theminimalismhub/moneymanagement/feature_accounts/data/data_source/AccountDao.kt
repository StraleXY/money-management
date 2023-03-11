package com.theminimalismhub.moneymanagement.feature_accounts.data.data_source

import androidx.room.*
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import kotlinx.coroutines.flow.Flow

@Dao
interface AccountDao {

    @Query("SELECT * FROM Account")
    fun getAll(): Flow<List<Account>>

    @Query("SELECT * FROM Account WHERE accountId = :id")
    suspend fun getAccountById(id: Int): Account?

    @Query("UPDATE Account SET balance = balance + :amount WHERE accountId = :id")
    suspend fun updateAccountBalance(amount: Double, id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account): Long

    @Transaction
    @Query("DELETE FROM Account WHERE accountId = :id")
    suspend fun deleteAccount(id: Int)

}