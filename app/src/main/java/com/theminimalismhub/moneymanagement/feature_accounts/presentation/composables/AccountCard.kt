package com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables

import android.graphics.Point
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.theminimalismhub.moneymanagement.core.composables.DashedBox
import com.theminimalismhub.moneymanagement.core.enums.AccountType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryAmount
import com.theminimalismhub.moneymanagement.ui.theme.credit_card
import com.theminimalismhub.moneymanagement.ui.theme.economica
import kotlin.random.Random

@Composable
fun AccountCardMini(
    modifier: Modifier = Modifier,
    account: Account,
    selected: Boolean,
    currency: String = "RSD",
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable (
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() }
            .alpha(animateFloatAsState(targetValue = if(selected) 1f else 0.5f).value),
        shape = RoundedCornerShape(15.dp),
        elevation = 8.dp
    ) {
        Row(
            modifier = Modifier
                .widthIn(120.dp)
                .padding(vertical = 18.dp, horizontal = 26.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = "${account.balance.toInt()} $currency",
                    style = MaterialTheme.typography.body1.copy(
                        fontSize = 24.sp,
                        lineHeight = 24.sp
                    )
                )
            }
            Spacer(modifier = Modifier.width(34.dp))
            AccountIcon(type = account.type)
        }
    }
}

@Composable
fun AccountCardLarge(
    modifier: Modifier = Modifier,
    account: Account,
    currency: String = "RSD",
    maxAmount: Double = 0.0,
    totalPerCategory: List<CategoryAmount> = emptyList(),
    scale: Float = 1f,
    overlayStrength: Float = 0.05f
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
                .width((260 * scale).dp)
                .height((165 * scale).dp)
                .drawBehind {

                    val width = size.width.toInt()
                    val height = size.height.toInt()
                    val spotOffset = 15.dp
                        .toPx()
                        .toInt()
                    val dotOffset = 50.dp
                        .toPx()
                        .toInt()
                    val dotsPerColor = 35
                    val dotOpacity = 0.05f
                    val inset = 30.dp
                        .toPx()
                        .toInt()

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
                        alpha = overlayStrength
                    )

                    if (totalPerCategory.isEmpty()) return@drawBehind

                    totalPerCategory
                        .filter { it.amount > maxAmount * 0.22 }
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
                    .padding(vertical = 28.dp, horizontal = 28.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 2.dp),
                    text = account.name,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = "${account.balance.toInt()} $currency",
                    style = MaterialTheme.typography.body1.copy(
                        fontFamily = economica,
                        fontSize = 40.sp
                    )
                )
            }
            if(account.type == AccountType.CARD) {
                CardNumber(
                    modifier = Modifier
                        .align(Alignment.BottomStart)
                        .padding(start = 28.dp, bottom = 25.dp),
                    lastDigits = account.description
                )
            }
            AccountIcon(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 20.dp),
                type = account.type
            )
        }
    }
}

@Composable
fun AddNewAccount(
    scale: Float = 1f,
    onClick: () -> Unit
) {
    DashedBox(
        modifier = Modifier
            .clip(RoundedCornerShape(15.dp))
            .clickable { onClick() }
            .width((260 * scale).dp)
            .height((165 * scale).dp)
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

@Composable
private fun CardNumber(
    modifier: Modifier = Modifier,
    lastDigits: String
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(
            modifier = Modifier.offset(0.dp, (-2).dp),
            text = "● ● ● ● ",
            style = TextStyle(
                fontFamily = credit_card,
                fontSize = 11.sp
            )
        )
        Text(
            text = lastDigits,
            style = TextStyle(
                fontFamily = credit_card,
                fontSize = 12.sp
            )
        )
    }
}

@Composable
private fun AccountIcon(
    modifier: Modifier = Modifier,
    type: AccountType
) {
    Icon(
        modifier = modifier,
        imageVector = when(type) {
            AccountType.CARD -> Icons.Default.CreditCard
            AccountType.CASH -> Icons.Default.AccountBalanceWallet
            else -> Icons.Default.CreditCard
        },
        contentDescription = Icons.Default.CreditCard.name
    )
}
