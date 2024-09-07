package com.theminimalismhub.moneymanagement.feature_bills.domain.model

import androidx.room.Embedded
import androidx.room.Relation
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_bills.data.model.BillItem
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import java.io.Serializable
import java.time.LocalDate
import java.util.Calendar

data class Bill(
    @Embedded var bill: BillItem,
    @Relation(
        parentColumn = "billCategoryId",
        entityColumn = "categoryId"
    )
    val category: Category,

    @Relation(
        parentColumn = "billAccountId",
        entityColumn = "accountId"
    )
    val account: Account,
) : Serializable {
    fun getPayedBill() : BillItem {
        val due: Calendar = when(bill.type) {
            RecurringType.MONTHLY -> {
                val calendar = Calendar.getInstance()
                calendar.add(Calendar.MONTH, 1)
                calendar.set(Calendar.DAY_OF_MONTH, bill.time)
                calendar
            }
            RecurringType.INTERVAL -> {
                val calendar = Calendar.getInstance()
                calendar.timeInMillis = bill.due
                calendar.add(Calendar.DAY_OF_MONTH, bill.time)
                calendar
            }
        }
        val isPaid: Boolean = when(bill.type) {
            RecurringType.MONTHLY -> true
            RecurringType.INTERVAL -> due.get(Calendar.MONTH) + 1 != LocalDate.now().month.value
        }
        return BillItem(
            name = bill.name,
            amount = bill.amount,
            time = bill.time,
            type = bill.type,
            due = due.timeInMillis,
            billCategoryId = category.categoryId!!,
            billAccountId = account.accountId!!,
            isLastMonthPaid = isPaid,
            billId = bill.billId
        )
    }

    fun checkIfCanBePayed() : Boolean {
        val due = Calendar.getInstance()
        due.timeInMillis = bill.due
        return due.get(Calendar.MONTH) + 1 == LocalDate.now().month.value
    }
}
