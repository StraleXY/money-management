package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.height
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.theminimalismhub.moneymanagement.core.composables.CRUDButtons
import com.theminimalismhub.moneymanagement.core.composables.FloatingCard

@Composable
fun AddEditFundCard(
    isOpen: Boolean
) {

    FloatingCard(
        visible = isOpen,
        header = {
            FundSelectorPager(

            )
        }
    ) {
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