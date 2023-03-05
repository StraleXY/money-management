package com.theminimalismhub.moneymanagement.feature_finances.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.theminimalismhub.moneymanagement.core.domain.model.FinanceType
import java.io.Serializable

@Entity
data class Finance(
    val name: String,
    val amount: Double,
    val timestamp: Long,
    val type: FinanceType,
    @PrimaryKey val categoryId: Int? = null,
    val financeCategoryId: Int,
    val financeAccountId: Int
) : Serializable
