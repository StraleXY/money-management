package com.theminimalismhub.moneymanagement.feature_funds.data.model

import androidx.room.Entity
import androidx.room.ForeignKey
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import org.jetbrains.annotations.NotNull

@Entity(
    tableName = "FundCategoryCrossRef",
    primaryKeys = ["fundId", "categoryId"],
    foreignKeys = [
        ForeignKey(entity = FundItem::class, parentColumns = ["fundId"], childColumns = ["fundId"]),
        ForeignKey(entity = Category::class, parentColumns = ["categoryId"], childColumns = ["categoryId"])
    ]
)
data class FundCategoryCrossRef(
    val fundId: Int,
    val categoryId: Int
)

@Entity(
    tableName = "FundAccountCrossRef",
    primaryKeys = ["fundId", "accountId"],
    foreignKeys = [
        ForeignKey(entity = FundItem::class, parentColumns = ["fundId"], childColumns = ["fundId"]),
        ForeignKey(entity = Account::class, parentColumns = ["accountId"], childColumns = ["accountId"])
    ]
)
data class FundAccountCrossRef(
    val fundId: Int,
    val accountId: Int
)

@Entity(
    tableName = "FundFinanceCrossRef",
    primaryKeys = ["fundId", "financeId"],
    foreignKeys = [
        ForeignKey(entity = FundItem::class, parentColumns = ["fundId"], childColumns = ["fundId"]),
        ForeignKey(entity = FinanceItem::class, parentColumns = ["financeId"], childColumns = ["financeId"])
    ]
)
data class FundFinanceCrossRef(
    val fundId: Int,
    val financeId: Int
)
