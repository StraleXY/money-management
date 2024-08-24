package com.theminimalismhub.moneymanagement.feature_bills.domain.model

import androidx.room.Embedded
import androidx.room.Relation
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_bills.data.model.BillItem
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import java.io.Serializable

data class Bill(
    @Embedded val bill: BillItem,
    @Relation(
        parentColumn = "billCategoryId",
        entityColumn = "categoryId"
    )
    val category: Category?,

    @Relation(
        parentColumn = "billAccountId",
        entityColumn = "accountId"
    )
    val account: Account,
) : Serializable
