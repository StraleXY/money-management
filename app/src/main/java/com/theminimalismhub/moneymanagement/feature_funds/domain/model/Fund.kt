package com.theminimalismhub.moneymanagement.feature_funds.domain.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
import com.theminimalismhub.moneymanagement.core.enums.FundType
import com.theminimalismhub.moneymanagement.core.enums.RecurringType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundAccountCrossRef
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundCategoryCrossRef
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundFinanceCrossRef
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundItem
import java.io.Serializable

data class Fund(
    @Embedded val item: FundItem,
    @Relation(
        parentColumn = "fundId",
        entityColumn = "categoryId",
        associateBy = Junction(FundCategoryCrossRef::class)
    )
    val categories: List<Category> = emptyList(),
    @Relation(
        parentColumn = "fundId",
        entityColumn = "accountId",
        associateBy = Junction(FundAccountCrossRef::class)
    )
    val accounts: List<Account> = emptyList(),
    @Relation(
        parentColumn = "fundId",
        entityColumn = "financeId",
        associateBy = Junction(FundFinanceCrossRef::class)
    )
    val finances: List<FinanceItem> = emptyList()
) : Serializable {

    companion object {
        fun getEmpty(): Fund {
            return Fund(
                item = FundItem(
                    name = "",
                    amount = 0.0,
                    type = FundType.BUDGET
                )
            )
        }
    }

    fun getRemaining() : Double {
        if (item.type != FundType.BUDGET) return 0.0
        if (item.recurringType == null) return item.amount
        return when(item.recurringType) {
            RecurringType.UNTIL_SPENT -> item.amount - finances.sumOf { it.amount }
            else -> {
                val range = item.recurringType.getTimeSpan()
                item.amount - finances.sumOf { if(range!!.first <= it.timestamp && it.timestamp <= range.second) it.amount else 0.0}
            }
        }
    }
}
