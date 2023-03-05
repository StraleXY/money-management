package com.theminimalismhub.moneymanagement.feature_finances.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import java.io.Serializable

@Entity
data class FinanceItem(
    val name: String,
    val amount: Double,
    val timestamp: Long,
    val type: FinanceType,
    @PrimaryKey val categoryId: Int? = null,
    val financeCategoryId: Int,
    val financeAccountId: Int
) : Serializable
