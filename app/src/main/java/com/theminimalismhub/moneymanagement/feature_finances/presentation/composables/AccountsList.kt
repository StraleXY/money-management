package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountCardMini

@Composable
fun AccountsList(
    modifier: Modifier = Modifier,
    accounts: List<Account>,
    states: HashMap<Int, MutableState<Boolean>>,
    contentPadding: PaddingValues = PaddingValues(horizontal = 32.dp),
    currency: String = "RSD",
    selectionChanged: (Int) -> Unit
) {
    LazyRow(
        modifier = modifier
            .fillMaxWidth(),
        contentPadding = contentPadding,
        horizontalArrangement = Arrangement.Start
    ) {
        items(accounts.filter { it.active }) { account ->
            AccountCardMini(
                account = account,
                selected = states[account.accountId]!!.value,
                currency = currency
            ) { selectionChanged(account.accountId!!) }
            Spacer(modifier = Modifier.width(16.dp))
        }
    }
}