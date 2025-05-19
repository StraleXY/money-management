package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.focusModifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import com.theminimalismhub.moneymanagement.core.enums.AccountType
import com.theminimalismhub.moneymanagement.core.enums.FundType
import com.theminimalismhub.moneymanagement.core.utils.Colorer
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.getAccountIcon
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund

@Composable
fun DisplayFundCard(
    modifier: Modifier = Modifier,
    fund: Fund
) {
    when(fund.item.type) {
        FundType.BUDGET -> BudgetFund(
            modifier = modifier,
            recurring = fund.item.recurringType?.toString()?.uppercase(),
            remaining = fund.item.amount.takeIf { it > 0.0 },
            amount = fund.item.amount.takeIf { it > 0.0 },
            name = fund.item.name.ifEmpty { null },
            colors = fund.categories.map { Colorer.getAdjustedDarkColor(it.color) }
        )
        FundType.RESERVATION -> ReservedFund(
            modifier = modifier,
            amount = fund.item.amount.takeIf { it > 0.0 },
            item = fund.item.name.ifEmpty { null },
            accountName = fund.accounts.firstOrNull()?.name,
            accountIcon = getAccountIcon(fund.accounts.firstOrNull()?.type ?: AccountType.UNKNOWN),
            categoryName = fund.categories.firstOrNull()?.name,
            categoryColor = Colorer.getAdjustedDarkColor(fund.categories.firstOrNull()?.color ?: Color.Gray.toArgb())
        )
        FundType.SAVINGS -> SavingsFund(
            modifier = modifier,
            amount = fund.item.amount.takeIf { it > 0.0 },
            saved = if(fund.item.amount > 0.0) 0.0 else null,
            name = fund.item.name.ifEmpty { null }
        )
    }
}