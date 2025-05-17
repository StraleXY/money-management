package com.theminimalismhub.moneymanagement.feature_funds.domain.model

import androidx.room.Embedded
import androidx.room.Junction
import androidx.room.Relation
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
) : Serializable
