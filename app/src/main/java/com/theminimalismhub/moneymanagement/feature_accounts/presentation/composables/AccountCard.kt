package com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables

import android.graphics.Point
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.theminimalismhub.moneymanagement.core.composables.DashedBox
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryEarnings
import com.theminimalismhub.moneymanagement.ui.theme.economica
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
    val random = Random(System.currentTimeMillis()) //21132020

    fun normalizeScaled(x: Double) : Double {
        return Math.max(Math.min(Math.log(x + 0.65) + 0.5, 1.0), 0.0)
    }
    fun calc(x: Double) : Float {
        val fraction = normalizeScaled((x / maxAmount.coerceAtLeast(1.0)))
        return fraction.toFloat().coerceAtLeast(0.1f)
    }

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(15.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .width(260.dp)
                .height(165.dp)
                .drawBehind {

                    val width = size.width.toInt()
                    val height = size.height.toInt()
                    val spotOffset = 15.dp.toPx().toInt()
                    val dotOffset = 50.dp.toPx().toInt()
                    val dotsPerColor = 35
                    val dotOpacity = 0.05f
                    val inset = 30.dp.toPx().toInt()

                    val possibleSpots = mutableListOf(
                        Point(inset, inset),
                        Point(width - inset, inset),
                        Point(width / 2, inset),
                        Point(inset, height - inset),
                        Point(inset, height / 2),
                        Point(width - inset, height - inset)
                    )

                    drawRect(
                        color = Color.White,
                        size = size,
                        alpha = 0.05f
                    )

                    if (totalPerCategory.isEmpty()) return@drawBehind

                    totalPerCategory
                        .filter { it.amount > maxAmount * 0.25 }
                        .forEach {
                            if (possibleSpots.isEmpty()) return@drawBehind

                            val spot = possibleSpots.last()
                            possibleSpots.remove(spot)

                            val center = Offset(
                                random.nextFloat() * random.nextInt(
                                    -spotOffset,
                                    spotOffset
                                ) + spot.x,
                                random.nextFloat() * random.nextInt(
                                    -spotOffset,
                                    spotOffset
                                ) + spot.y
                            )
                            val radius = (calc(it.amount) * 120.dp.toPx()).coerceAtLeast(0f)

                            for (i in 1..dotsPerColor) {
                                val normalizedDotOffset = normalizeScaled(it.amount) * dotOffset
                                val calcOffset =
                                    (normalizedDotOffset * if (i > normalizedDotOffset / 5) (i / 70f) else 1f).toInt()
                                val offsetCenter = Offset(
                                    center.x + random.nextInt(-calcOffset, calcOffset),
                                    center.y + random.nextInt(-calcOffset, calcOffset)
                                )
                                val adjustedRadius = radius * random.nextInt(90, 125) / 100f
                                drawCircle(
                                    brush = Brush.radialGradient(
                                        Pair(0.2f, Color(it.color)), Pair(0.92f, Color.Transparent),
                                        radius = adjustedRadius,
                                        center = offsetCenter
                                    ),
                                    radius = adjustedRadius,
                                    center = offsetCenter,
                                    alpha = dotOpacity
                                )
                            }
                        }
                },
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 28.dp, horizontal = 28.dp),
                verticalArrangement = Arrangement.Bottom
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 4.dp),
                    text = account.name,
                    style = MaterialTheme.typography.body2.copy(
                        fontSize = 19.sp
                    )
                )
                Text(
                    text = "${account.balance.toInt()} $currency",
                    style = MaterialTheme.typography.body1.copy(
                        fontFamily = economica,
                        fontSize = 40.sp
                    )
                )
            }

        }
    }
}

@Composable
fun AddNewAccount(
    onClick: () -> Unit
) {
    DashedBox(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable { onClick() }
            .width(260.dp)
            .height(165.dp)
    ) {
        Column(
            modifier = Modifier,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                imageVector = Icons.Default.AddCard,
                contentDescription = Icons.Default.Add.name
            )
            Spacer(modifier = Modifier.height(12.dp))
            Text(
                text = "ADD ACCOUNT",
                style = MaterialTheme.typography.button,
                color = MaterialTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.height(6.dp))
            Text(
                text = "Accounts can be use to track savings too.",
                style = MaterialTheme.typography.subtitle2,
                color = MaterialTheme.colors.secondary
            )
        }
    }
}