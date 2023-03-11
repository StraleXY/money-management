package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun QuickSpendingOverview(
    modifier: Modifier = Modifier,
    amount: Double,
    rangeLength: Int,
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
                secondaryAmount = if(rangeLength == 1) 0.0 else amount / rangeLength
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
                amount = limit * rangeLength - amount,
                hint = "LIMIT",
                secondaryAmount = limit * rangeLength
            )
        }
    }
    Spacer(modifier = Modifier.height(12.dp))
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
    val animatedAmount by animateIntAsState(targetValue = amount.toInt(), tween(450))
    val animatedSecondaryAmount by animateIntAsState(targetValue = secondaryAmount.toInt(), tween(450))

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
            text = "$animatedAmount $currency",
            style = MaterialTheme.typography.h3.copy(
                fontSize = 48.sp
            ),
            color = if(amount < 0.0) MaterialTheme.colors.error else MaterialTheme.colors.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier
                .alpha(0.65f),
            text = "$hint: ${if(secondaryAmount == 0.0) "--" else animatedSecondaryAmount } $currency",
            style = MaterialTheme.typography.h3.copy(
                fontSize = 16.sp
            )
        )
    }
}