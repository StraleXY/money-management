package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.ui.theme.economica

@Composable
fun QuickSpendingOverview(
    modifier: Modifier = Modifier,
    amount: Double,
    average: Double = 0.0,
    limit: Double = 0.0
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(28.dp)
        ) {
            SpendingSegment(
                modifier = Modifier
                    .weight(0.49f, true)
                    .height(125.dp),
                title = "SPENT",
                amount = amount,
                hint = "AVERAGE",
                secondaryAmount = average
            )
            Spacer(modifier = Modifier.width(16.dp))
            Divider(
                modifier = Modifier
                    .width(1.dp)
                    .height(125.dp)
            )
            Spacer(modifier = Modifier.width(16.dp))
            SpendingSegment(
                modifier = Modifier
                    .weight(0.49f, true)
                    .height(125.dp),
                title = "REMAINING",
                amount = amount,
                hint = "LIMIT",
                secondaryAmount = limit
            )
        }
    }
}

@Composable
private fun SpendingSegment(
    modifier: Modifier = Modifier,
    title: String,
    amount: Double,
    hint: String,
    secondaryAmount: Double,
    currency: String = "RSD"
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.body1
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = "${amount.toInt()} $currency",
            style = MaterialTheme.typography.h3.copy(
                fontSize = 48.sp
            )
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier
                .alpha(0.65f),
            text = "$hint: ${if(secondaryAmount == 0.0) "--" else amount.toInt() } $currency",
            style = MaterialTheme.typography.h3.copy(
                fontSize = 16.sp
            )
        )
    }
}