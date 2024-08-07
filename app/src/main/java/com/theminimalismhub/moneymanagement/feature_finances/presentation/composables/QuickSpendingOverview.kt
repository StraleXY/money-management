package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.core.utils.Currencier

@Composable
fun QuickSpendingOverview(
    modifier: Modifier = Modifier,
    amount: Double,
    rangeLength: Int,
    limit: Double = 0.0,
    limitHidden: Boolean = false,
    currency: String = "RSD"
) {
    var width by remember { mutableStateOf(0.dp) }
    val density = LocalDensity.current

    Card(
        modifier = modifier
            .onGloballyPositioned { coordinates ->
                width = with(density) {
                    coordinates.size.width.toDp()
                }
            },
        shape = RoundedCornerShape(15.dp),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 18.dp)
        ) {
            SpendingSegment(
                modifier = Modifier
                    .weight(0.49f, true)
                    .height(125.dp),
                title = if(limitHidden) "TOTAL" else "SPENT",
                amount = amount,
                hint = "AVERAGE",
                secondaryAmount = if(rangeLength == 1) 0.0 else amount / rangeLength,
                currency = currency
            )
            AnimatedVisibility(
                visible = !limitHidden,
                enter = expandHorizontally(tween(300)) { 0 } + fadeIn(tween(200, 100)),
                exit = fadeOut(tween(200)) + shrinkHorizontally(tween(300, 100)) { 0 }
            ) {
                Spacer(modifier = Modifier.width(16.dp))
                Divider(
                    modifier = Modifier
                        .width(1.dp)
                        .height(125.dp)
                )
                Spacer(modifier = Modifier.width(16.dp))
                SpendingSegment(
                    modifier = Modifier
//                        .weight(0.49f, true)
                        .width(width / 2)
                        .height(125.dp),
                    title = "REMAINING",
                    amount = limit * rangeLength - amount,
                    hint = "LIMIT",
                    secondaryAmount = limit * rangeLength,
                    currency = currency
                )
            }

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
    val animatedAmount by
        if(Currencier.isDecimal(amount)) animateFloatAsState(targetValue = amount.toFloat(), tween(750))
        else animateIntAsState(targetValue = amount.toInt(), tween(750))

    val animatedSecondaryAmount by
        if(Currencier.isDecimal(secondaryAmount)) animateFloatAsState(targetValue = secondaryAmount.toFloat(), tween(750))
        else animateIntAsState(targetValue = secondaryAmount.toInt(), tween(750))

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
            text = "${Currencier.formatAmount(animatedAmount.toFloat())} $currency ",
            style = MaterialTheme.typography.h3.copy(
                fontSize = 45.sp
            ),
            color = if(amount < 0.0) MaterialTheme.colors.error else MaterialTheme.colors.onBackground
        )
        Spacer(modifier = Modifier.height(8.dp))
        Text(
            modifier = Modifier
                .alpha(0.65f),
            text = "$hint: ${if(secondaryAmount == 0.0) "--" else Currencier.formatAmount(animatedSecondaryAmount.toFloat()) } $currency",
            style = MaterialTheme.typography.h3.copy(
                fontSize = 15.sp
            )
        )
    }
}