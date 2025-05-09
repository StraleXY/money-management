package com.theminimalismhub.moneymanagement.feature_finances.domain.model

import androidx.room.Embedded
import androidx.room.Relation
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_finances.data.model.RecommendedFinanceItem

data class RecommendedFinance(
    @Embedded val recommended: RecommendedFinanceItem,
    @Relation(
        parentColumn = "financeCategoryId",
        entityColumn = "categoryId"
    )
    val category: Category?,
    @Relation(
        parentColumn = "financeAccountId",
        entityColumn = "accountId"
    )
    val account: Account?

)
