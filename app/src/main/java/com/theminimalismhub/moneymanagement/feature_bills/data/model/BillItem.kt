package com.theminimalismhub.moneymanagement.feature_bills.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import com.theminimalismhub.moneymanagement.feature_bills.domain.model.RecurringType
import java.io.Serializable

@Entity(tableName = "Bill")
data class BillItem (
    val name: String,
    val amount: Double,
    val time: Int,
    val type: RecurringType,
    val due: Long,
    val billCategoryId: Int,
    val billAccountId: Int,
    val isLastMonthPaid: Boolean,
    @PrimaryKey val billId: Int? = null
) : Serializable