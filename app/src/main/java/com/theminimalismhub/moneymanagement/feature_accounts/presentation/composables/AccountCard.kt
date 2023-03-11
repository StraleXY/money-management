package com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables

import android.graphics.Point
import android.util.Log
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.center
import androidx.compose.ui.graphics.BlendMode
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryEarnings
import kotlin.random.Random

@Composable
fun AccountCardMini(
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
                text = "${account.balance.toInt()} $currency",
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 24.sp
                )
            )
        }
    }
}

@Composable
fun AccountCardLarge(
    modifier: Modifier = Modifier,
    account: Account,
    currency: String = "RSD",
    maxAmount: Double,
    totalPerCategory: List<CategoryEarnings>
) {
    val random = Random(21132020) // System.currentTimeMillis()

    fun normalize(x: Double) : Double {
        return Math.max(Math.min(Math.log(x + 0.65) + 0.5, 1.0), 0.0)
    }
    fun calc(x: Double) : Float {
        val fraction = normalize((x / maxAmount.coerceAtLeast(1.0)))
        return fraction.toFloat().coerceAtLeast(0.1f)
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        elevation = 8.dp
    ) {
        Column(
            modifier = Modifier
                .padding(vertical = 32.dp, horizontal = 36.dp)
                .widthIn(215.dp)
                .heightIn(135.dp)
                .drawBehind {

                    val width = size.width.toInt()
                    val height = size.height.toInt()
                    if (totalPerCategory.isEmpty()) return@drawBehind

                    val possibleSpots = listOf( Point(0, 0), Point(height, width), Point(height,  0), Point( 0,  width))

                    totalPerCategory.filter { it.amount > maxAmount * 0.2 }.forEach {
                        if(possibleSpots.isEmpty()) return@drawBehind

                        val center = Offset(random.nextFloat() * width, random.nextFloat() * height)
                        val radius = (calc(it.amount) * 120.dp.toPx()).coerceAtLeast(0f)

                        for(i in 1..5) {
                            val offsetCenter = Offset(center.x + random.nextInt(
                                (-i*radius/3).toInt(), (i*radius/3).toInt()), center.y + random.nextInt(-i*50, i*50))
                            drawCircle(
                                brush = Brush.radialGradient(
                                    Pair(0.2f, Color(it.color)), Pair(0.92f, Color.Transparent),
                                    radius = radius,
                                    center = offsetCenter
                                ),
                                radius = radius,
                                center = offsetCenter,
                                alpha = 0.2f
                            )
                        }
                    }
                }
        ) {
            Text(
                text = account.name,
                style = MaterialTheme.typography.body2.copy(
                    fontSize = 18.sp
                )
            )
            Text(
                text = "${account.balance.toInt()} $currency",
                style = MaterialTheme.typography.body1.copy(
                    fontSize = 24.sp
                )
            )
        }
    }
}