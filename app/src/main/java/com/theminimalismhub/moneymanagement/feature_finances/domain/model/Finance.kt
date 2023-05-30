package com.theminimalismhub.moneymanagement.feature_finances.domain.model

import android.annotation.SuppressLint
import androidx.room.Embedded
import androidx.room.Relation
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_finances.data.model.FinanceItem
import java.io.Serializable
import java.text.SimpleDateFormat
import java.util.*

data class Finance(
    @Embedded val finance: FinanceItem,
    @Relation(
        parentColumn = "financeCategoryId",
        entityColumn = "categoryId"
    )
    val category: Category?,
    @Relation(
        parentColumn = "financeAccountId",
        entityColumn = "accountId"
    )
    val account: Account,
    @Relation(
        parentColumn = "financeAccountIdFrom",
        entityColumn = "accountId"
    )
    val accountTo: Account?,

) : Serializable {
    fun getDay() : Int {
        val time = Calendar.getInstance()
        time.timeInMillis = finance.timestamp
        return time.get(Calendar.DAY_OF_MONTH)
    }
    @SuppressLint("SimpleDateFormat")
    fun getMonth() : String {
        val time = Calendar.getInstance()
        time.time = Date(finance.timestamp)
        return SimpleDateFormat("MMM").format(time.time)
    }
}
