package com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account

@Composable
fun AccountCard(
    modifier: Modifier = Modifier,
    account: Account,
    currency: String = "RSD"
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 18.dp, horizontal = 22.dp)
                .widthIn(120.dp)
        ) {
            Text(
                text = account.name,
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 18.sp
                )
            )
            Text(
                text = "${account.balance.toInt()} RSD",
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 24.sp
                )
            )
        }
    }
}