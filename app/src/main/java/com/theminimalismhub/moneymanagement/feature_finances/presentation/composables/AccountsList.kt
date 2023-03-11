package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountCardMini

@Composable
fun AccountsList(
    modifier: Modifier = Modifier,
    accounts: List<Account>
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = PaddingValues(horizontal = 32.dp),
        horizontalArrangement = Arrangement.Start
    ) {
        items(accounts.filter { it.active }) { account ->
            AccountCardMini(account = account)
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}