package com.theminimalismhub.moneymanagement.feature_finances.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import java.io.Serializable

@Entity(tableName = "RecommendedFinance")
data class RecommendedFinanceItem(
    @PrimaryKey val recommendedId: Int? = null,
    val placeName: String,
    val accountLabel: String,
    val amount: Double,
    val currencyStr: String,
    val timestamp: Long,
    val type: FinanceType = FinanceType.OUTCOME,
    val financeCategoryId: Int? = null,
    val financeAccountId: Int? = null,
    val trackable: Boolean = true,
    val financeItemId: Int? = null
) : Serializable
