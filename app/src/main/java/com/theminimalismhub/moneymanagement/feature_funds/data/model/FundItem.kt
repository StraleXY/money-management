package com.theminimalismhub.moneymanagement.feature_funds.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.theminimalismhub.moneymanagement.core.enums.FundType
import com.theminimalismhub.moneymanagement.core.enums.RecurringType
import java.io.Serializable

@Entity(tableName = "Fund")
data class FundItem(
    @PrimaryKey val fundId: Int? = null,
    val name: String,
    val type: FundType,
    val amount: Double,
    val startDate: Long? = null,
    val recurringType: RecurringType? = null,
    val endDate: Long? = null,
    val endType: FundType? = null
): Serializable
