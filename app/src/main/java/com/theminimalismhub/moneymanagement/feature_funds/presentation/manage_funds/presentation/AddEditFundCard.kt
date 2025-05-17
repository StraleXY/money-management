package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation

import android.util.Log
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.core.composables.CRUDButtons
import com.theminimalismhub.moneymanagement.core.composables.FloatingCard
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_finances.presentation.composables.AccountsChipsSelectable

@Composable
fun AddEditFundCard(
    isOpen: Boolean,
    accounts: List<Account>
) {

    FloatingCard(
        visible = isOpen,
        header = {
            FundSelectorPager(

            )
        }
    ) {
        Spacer(modifier = Modifier.height(8.dp))
        AccountsChipsSelectable(
            accounts = accounts,
            selectionChanged = { selected -> Log.d("Account Selection", selected.toString())}
        )
        Spacer(modifier = Modifier.height(8.dp))
        CRUDButtons(
            onSave = {

            },
            deleteEnabled = false,
            onDelete = {

            },
            onCancel = {

            }
        )
        Spacer(modifier = Modifier.height((9.5).dp))
    }
}