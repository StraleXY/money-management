package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountCardLarge
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AddNewAccount

@Composable
@Destination(style = BaseTransition::class)
fun ManageAccountsScreen(
    vm: ManageAccountsViewModel = hiltViewModel()
) {

    val state = vm.state.value

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                ScreenHeader(
                    title = "Management Accounts",
                    hint = "Track your balance across multiple accounts!"
                )
                LazyRow(
                    modifier = Modifier.fillMaxWidth(),
                    contentPadding = PaddingValues(horizontal = 24.dp),
                    horizontalArrangement = Arrangement.Start
                ) {
                    items(state.accounts) { account ->
                        AccountCardLarge(account = account)
                        Spacer(modifier = Modifier.width(8.dp))
                    }
                    item {
                        AddNewAccount {

                        }
                    }
                }
                Spacer(modifier = Modifier.height(24.dp))
            }
        }
    }
}