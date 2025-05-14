package com.theminimalismhub.moneymanagement.feature_funds.data.data_source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Transaction
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundAccountCrossRef
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundCategoryCrossRef
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundFinanceCrossRef
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundItem
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund

@Dao
interface FundDao {

    @Insert
    suspend fun insertFund(fund: FundItem): Long

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertCategoryRef(crossRef: FundCategoryCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAccountRef(crossRef: FundAccountCrossRef)

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertFinanceRef(crossRef: FundFinanceCrossRef)

    @Transaction
    @Query("SELECT * FROM Fund ")
    suspend fun getFunds(): List<Fund>

}