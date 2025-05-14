package com.theminimalismhub.moneymanagement.feature_funds.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import androidx.room.Update
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundAccountCrossRef
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundCategoryCrossRef
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundFinanceCrossRef
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundItem
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import kotlinx.coroutines.flow.Flow

@Dao
interface FundDao {

    @Insert
    suspend fun insertFund(fund: FundItem): Long

    @Update
    suspend fun updateFund(fund: FundItem)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategoryRef(crossRef: FundCategoryCrossRef)

    @Query("DELETE FROM FundCategoryCrossRef WHERE fundId = :fundId")
    suspend fun deleteCategoryRefs(fundId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAccountRef(crossRef: FundAccountCrossRef)

    @Query("DELETE FROM FundAccountCrossRef WHERE fundId = :fundId")
    suspend fun deleteAccountRefs(fundId: Int)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFinanceRef(crossRef: FundFinanceCrossRef)

    @Query("DELETE FROM FundFinanceCrossRef WHERE fundId = :fundId")
    suspend fun deleteFinanceRefs(fundId: Int)

    @Transaction
    @Query("SELECT * FROM Fund ")
    fun getFunds(): Flow<List<Fund>>

}