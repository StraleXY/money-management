package com.theminimalismhub.moneymanagement.feature_bills.presentation

import com.theminimalismhub.moneymanagement.core.enums.AccountType
import com.theminimalismhub.moneymanagement.core.enums.FinanceType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_bills.data.model.BillItem
import com.theminimalismhub.moneymanagement.feature_bills.domain.model.Bill
import com.theminimalismhub.moneymanagement.feature_bills.domain.model.RecurringType
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category

data class ManageBillsState(
    val isAddEditOpen: Boolean = false,
    val bills: List<Bill> = listOf(
        Bill(
            bill = BillItem(
                name = "YT Premium",
                amount = 250.0,
                time = 8,
                type = RecurringType.MONTHLY,
                due = 1725800442000,
                billCategoryId = 0,
                billAccountId = 0,
                isLastMonthPaid = false,
                billId = 0
            ),
            account = Account(
                name = "VISA",
                balance = 71000.0,
                active = true,
                deleted = false,
                primary = false,
                type = AccountType.CARD,
                description = ""
            ),
            category = Category(
                name = "Racuni",
                color = -15073305,
                type = FinanceType.OUTCOME,
                isDeleted = false
            )
        )
    )
)
