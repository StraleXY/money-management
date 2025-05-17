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
            modifier = modifier
        )
        FundType.RESERVATION -> ReservedFund(
            modifier = modifier,
            amount = fund.item.amount,
            item = fund.item.name,
            accountName = fund.accounts.firstOrNull()?.name ?: "Unknown",
            accountIcon = getAccountIcon(fund.accounts.firstOrNull()?.type ?: AccountType.UNKNOWN),
            categoryName = fund.categories.firstOrNull()?.name ?: "Unknown",
            categoryColor = Colorer.getAdjustedDarkColor(fund.categories.firstOrNull()?.color ?: Color.Gray.toArgb())
        )
        FundType.SAVINGS -> SavingsFund(
            modifier = modifier
        )
    }
}