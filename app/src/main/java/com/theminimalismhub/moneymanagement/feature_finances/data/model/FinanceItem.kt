package com.theminimalismhub.moneymanagement.feature_finances.data.model

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import java.io.Serializable

@Entity(tableName = "Finance")
data class FinanceItem(
    val name: String,
    val amount: Double,
    val timestamp: Long,
    val type: FinanceType,
    @PrimaryKey val id: Int? = null,
    val financeCategoryId: Int? = null,
    val financeAccountId: Int,
    @ColumnInfo(name = "financeAccountIdFrom", defaultValue = "NULL") val financeAccountIdFrom: Int? = null
) : Serializable
