package com.theminimalismhub.moneymanagement.feature_finances.presentation.composables

import android.util.Half.toFloat
import androidx.compose.animation.*
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.animateIntAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Divider
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDownward
import androidx.compose.material.icons.filled.ArrowUpward
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.core.utils.Currencier
import kotlin.math.absoluteValue

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
            modifier = Modifier.padding(vertical = 18.dp)
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
fun SpendingSegment(
    modifier: Modifier = Modifier,
    title: String,
    amount: Double,
    hint: String = "",
    secondaryAmount: Double? = null,
    currency: String = "RSD"
) {
    val animatedAmount by
        if(Currencier.isDecimal(amount)) animateFloatAsState(targetValue = amount.toFloat(), tween(750))
        else animateIntAsState(targetValue = amount.toInt(), tween(750))

    val animatedSecondaryAmount by
        if(Currencier.isDecimal(secondaryAmount ?: 0.0)) animateFloatAsState(targetValue = secondaryAmount?.toFloat() ?: 0f, tween(750))
        else animateIntAsState(targetValue = secondaryAmount?.toInt() ?: 0, tween(750))

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
        secondaryAmount?.let {
            Text(
                modifier = Modifier
                    .alpha(0.65f),
                text = "$hint: ${if (secondaryAmount == 0.0) "--" else Currencier.formatAmount(animatedSecondaryAmount.toFloat())} $currency",
                style = MaterialTheme.typography.h3.copy(
                    fontSize = 15.sp
                )
            )
        }
    }
}

@Composable
fun QuickSpendingOverviewCompact(
    modifier: Modifier = Modifier,
    amount: Double,
    average: Double,
    rangeLength: Int,
    limit: Double = 0.0,
    limitHidden: Boolean = false,
    currency: String = "RSD"
) {
    fun increase() : Int {
        return try {
            ((amount - average * rangeLength) / (average * rangeLength) * 100).toInt()
        } catch (ex: Exception) {
            0
        }
    }

    val percent by remember(amount) { mutableStateOf(increase()) }

    val animatedAmount by
        if(Currencier.isDecimal(amount)) animateFloatAsState(targetValue = amount.toFloat(), tween(750))
        else animateIntAsState(targetValue = amount.toInt(), tween(750))

    val animatedPercent by animateFloatAsState(targetValue = percent.absoluteValue.toFloat(), tween(750))
    
    val animatedBackground by animateColorAsState(targetValue = if(percent > 0) Color(209, 59, 21, 100) else Color(111, 176, 62, 100))
    val animatedForeground by animateColorAsState(targetValue = if(percent > 0) Color(232, 210, 204, 255) else Color(232, 245, 223, 255))

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(15.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier.padding(20.dp)
        ) {
            Text(
                text = if(limitHidden) "Earned:" else "Spent:",
                style = MaterialTheme.typography.body1,
                modifier = Modifier
                    .padding(start = 4.dp)
                    .alpha(0.75f)
            )
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "${animatedAmount.toInt()} $currency",
                    style = MaterialTheme.typography.h2,
                    color = MaterialTheme.colors.onBackground
                )
                Spacer(modifier = Modifier.width(12.dp))
                if(!limitHidden) Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(100))
                        .background(animatedBackground),
                ) {
                    Row(
                        modifier = Modifier
                            .height(36.dp)
                            .padding(start = 8.dp, end = 12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = if (percent > 0) Icons.Default.ArrowUpward else Icons.Default.ArrowDownward,
                            contentDescription = "Upper-Lower icon",
                            tint = animatedForeground
                        )
                        Text(
                            text = "${animatedPercent.toInt()}%",
                            style = MaterialTheme.typography.body1,
                            color = animatedForeground
                        )
                    }
                }
            }
        }
    }
}