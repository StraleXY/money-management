package com.theminimalismhub.moneymanagement.feature_accounts.data.data_source

import androidx.room.*
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import kotlinx.coroutines.flow.Flow
import org.jetbrains.annotations.NotNull

@Dao
interface AccountDao {

    @Query("SELECT * FROM Account WHERE deleted = 0")
    fun getAll(): Flow<List<Account>>

    @Query("SELECT * FROM Account WHERE accountId = :id")
    suspend fun getAccountById(id: Int): Account?

    @Query("UPDATE Account SET balance = balance + :amount WHERE accountId = :id")
    suspend fun updateAccountBalance(amount: Double, id: Int)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertAccount(account: Account): Long

    @Query("UPDATE Account SET `primary` = CASE WHEN accountId = :id THEN 1 ELSE 0 END")
    suspend fun setPrimary(id: Int)
}