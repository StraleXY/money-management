package com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables

import android.graphics.Point
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.keyframes
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Card
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.RadioButton
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountBalance
import androidx.compose.material.icons.filled.AccountBalanceWallet
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCard
import androidx.compose.material.icons.filled.CreditCard
import androidx.compose.material.icons.filled.CurrencyBitcoin
import androidx.compose.material.icons.filled.MonitorHeart
import androidx.compose.material.icons.filled.QuestionMark
import androidx.compose.material.icons.filled.Savings
import androidx.compose.material.icons.filled.SupervisorAccount
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.node.modifierElementOf
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import com.theminimalismhub.moneymanagement.R
import com.theminimalismhub.moneymanagement.core.composables.DashedBox
import com.theminimalismhub.moneymanagement.core.composables.SelectableChip
import com.theminimalismhub.moneymanagement.core.enums.AccountType
import com.theminimalismhub.moneymanagement.core.utils.Currencier
import com.theminimalismhub.moneymanagement.core.utils.Shade
import com.theminimalismhub.moneymanagement.core.utils.getShadedColor
import com.theminimalismhub.moneymanagement.core.utils.shadedBackground
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_finances.presentation.home.CategoryAmount
import com.theminimalismhub.moneymanagement.ui.theme.credit_card
import com.theminimalismhub.moneymanagement.ui.theme.economica
import kotlinx.coroutines.launch
import kotlin.math.log2
import kotlin.random.Random

@Composable
fun AccountChip(
    modifier: Modifier = Modifier.widthIn(60.dp, 180.dp),
    account: Account,
    selected: Boolean = true,
    onClick: () -> Unit
) {
    SelectableChip(
        modifier = modifier,
        label = account.name,
        icon = getAccountIcon(account.type),
        onClick = onClick,
        selected = selected
    )
}

@Composable
fun SelectableAccountChipLarge(
    modifier: Modifier = Modifier,
    account: Account,
    selected: Boolean,
    currency: String = "RSD",
    clicked: () -> Unit
) {

    Box(
        modifier = modifier
            .height(64.dp)
            .clip(RoundedCornerShape(100))
            .background(animateColorAsState(if(selected) getShadedColor(Shade.LIGHT) else getShadedColor(Shade.DARK), tween(200)).value)
            .border(if(selected) 0.dp else 2.dp, color = if(selected) Color.Transparent else MaterialTheme.colors.secondaryVariant, RoundedCornerShape(100))
            .clickable { clicked() }
    ) {
        Row(
            modifier = modifier
                .fillMaxHeight()
                .padding(end = 24.dp, start = 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AccountIcon(
                modifier = Modifier.scale(1f),
                type = account.type,
                color = MaterialTheme.colors.onBackground
            )
            Spacer(modifier = Modifier.width(12.dp))
            Column {
                Text(
                    modifier = Modifier.offset(y = 2.dp),
                    text = account.name,
                    style = MaterialTheme.typography.h4
                )
                Text(
                    modifier = Modifier.offset(y = -2.dp),
                    text = if(account.type == AccountType.CRYPTO) "${stringResource(id = R.string.crypto_balance_mask)} $currency" else "${Currencier.formatAmount(account.balance)} $currency",
                    style = MaterialTheme.typography.body2,
                    color = MaterialTheme.colors.secondary
                )
            }
        }
    }
}

@Composable
fun AccountCardMini(
    modifier: Modifier = Modifier.widthIn(120.dp),
    account: Account,
    balance: Double = account.balance,
    selected: Boolean = true,
    currency: String = "RSD",
    onClick: () -> Unit
) {
    Card(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(24.dp),
        elevation = 8.dp,
        backgroundColor = Color(ColorUtils.blendARGB(MaterialTheme.colors.surface.toArgb(), Color.Black.toArgb(), if(selected) 0f else 0.1f))
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 18.dp, horizontal = 26.dp)
                .alpha(animateFloatAsState(targetValue = if (selected) 1f else 0.5f).value),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Text(
                    text = account.name,
                    style = MaterialTheme.typography.body2
                )
                Text(
                    text = if(account.type == AccountType.CRYPTO) "${stringResource(id = R.string.crypto_balance_mask)} $currency" else "${Currencier.formatAmount(balance)} $currency",
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
fun AccountTypeCard(
    modifier: Modifier = Modifier,
    type: AccountType,
    selected: Boolean,
    onClick: () -> Unit
) {
    val background = Color(ColorUtils.blendARGB(MaterialTheme.colors.surface.toArgb(), MaterialTheme.colors.onSurface.toArgb(), 0.18f))
    val foreground = MaterialTheme.colors.onBackground
    val animatedBackground = animateColorAsState(targetValue = if(selected) foreground else background)
    val animatedForeground = animateColorAsState(targetValue = if(selected) MaterialTheme.colors.background else foreground)

    Card(
        modifier = modifier
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null
            ) { onClick() },
        shape = RoundedCornerShape(100),
        elevation = 4.dp,
        backgroundColor = animatedBackground.value
    ) {
        Row(
            modifier = Modifier
                .padding(vertical = 10.dp)
                .padding(start = 22.dp, end = 18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = type.title,
                style = MaterialTheme.typography.body1,
                color = animatedForeground.value
            )
            Spacer(modifier = Modifier.width(12.dp))
            AccountIcon(
                modifier = Modifier.size(20.dp),
                type = type,
                color = animatedForeground.value
            )
        }
    }
}

@Composable
fun AccountCardLarge(
    modifier: Modifier = Modifier,
    account: Account,
    balanceDelta: Double = 0.0,
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
        shape = RoundedCornerShape(20.dp),
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
                    .padding(28.dp)
            ) {
                Text(
                    modifier = Modifier
                        .padding(start = 2.dp),
                    text = account.name,
                    style = MaterialTheme.typography.body1
                )
                Text(
                    text = if(account.type == AccountType.CRYPTO) "${stringResource(id = R.string.crypto_balance_mask)} $currency" else "${Currencier.formatAmount(account.balance + balanceDelta)} $currency",
                    style = MaterialTheme.typography.body1.copy(
                        fontFamily = economica,
                        fontSize = 40.sp
                    ),
                    color = if(account.type != AccountType.CRYPTO && (account.balance + balanceDelta).toInt() < 0) MaterialTheme.colors.error else MaterialTheme.colors.onBackground
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
fun AccountCardSwipeable(
    modifier: Modifier = Modifier,
    account: Account,
    balanceDelta: Double = 0.0,
    currency: String = "RSD",
    scale: Float = 1f
) {

    Card(
        modifier = modifier,
        shape = RoundedCornerShape(20.dp),
        backgroundColor = MaterialTheme.colors.surface,
        elevation = 8.dp
    ) {
        Box(
            modifier = Modifier
                .width((280 * scale).dp)
                .height((125 * scale).dp)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(24.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        modifier = Modifier.padding(start = 2.dp),
                        text = account.name,
                        style = MaterialTheme.typography.h4.copy(fontSize = 20.sp)
                    )

                }
                Row(verticalAlignment = Alignment.CenterVertically) {
                    if(account.type == AccountType.CARD) {
                        CardNumber(
                            modifier = Modifier.scale(0.925f),
                            lastDigits = account.description
                        )
                    }
                    else AccountIcon(type = account.type)

                }
            }
            Text(
                modifier = Modifier
                    .padding(start = 24.dp, bottom = 32.dp)
                    .align(Alignment.BottomStart),
                text = if(account.type == AccountType.CRYPTO) "${stringResource(id = R.string.crypto_balance_mask)} $currency" else "${Currencier.formatAmount(account.balance + balanceDelta)} $currency",
                style = MaterialTheme.typography.body1.copy(
                    fontFamily = economica,
                    fontSize = 40.sp
                ),
                color = if(account.type != AccountType.CRYPTO && (account.balance + balanceDelta).toInt() < 0) MaterialTheme.colors.error else MaterialTheme.colors.onBackground
            )
        }
    }
}

@Composable
fun NewAccountExampleCard(
    modifier: Modifier = Modifier,
    name: String,
    balance: String,
    type: AccountType,
    description: String,
    currency: String,
    scale: Float = 1f,
    overlayStrength: Float = 0.05f
) {

    var descriptionVisible by remember { mutableStateOf(type == AccountType.CARD) }
    val animatedRotationX = remember { Animatable(0f) }
    val animatedRotationY = remember { Animatable(0f) }
    val scope = rememberCoroutineScope()

    fun calc(len: Int) : Float {
        return -log2(len + 7f) + 8f
    }

    fun animate(x: Float, y: Float) {
        scope.launch {
            animatedRotationX.stop()
            animatedRotationX.animateTo(0f, animationSpec = keyframes {
                0f at animatedRotationX.value.toInt()
                x at 100
                x * 0.02f at 1050
                0f at 1150
            })
        }
        scope.launch {
            animatedRotationY.stop()
            animatedRotationY.animateTo(0f, animationSpec = keyframes {
                0f at animatedRotationX.value.toInt()
                y at 100
                y * 0.02f at 1050
                0f at 1150
            })
        }
    }

    LaunchedEffect(name) { animate(5f, -calc(name.length)) }
    LaunchedEffect(balance) { animate(1f, -calc(name.length)) }
    LaunchedEffect(type) {
        animate(-5f, 5f)
        descriptionVisible = type == AccountType.CARD
    }
    LaunchedEffect(description) { animate(-5f, -5f) }

    Card(
        modifier = modifier
            .graphicsLayer {
                rotationX = animatedRotationX.value
                rotationY = animatedRotationY.value
            },
        shape = RoundedCornerShape(20.dp),
        backgroundColor = MaterialTheme.colors.background,
        elevation = 16.dp
    ) {
        Box(
            modifier = Modifier
                .width((260 * scale).dp)
                .height((165 * scale).dp)
                .background(Color(ColorUtils.setAlphaComponent(Color.White.toArgb(), (255L * overlayStrength).toInt()))),
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(vertical = 28.dp, horizontal = 28.dp)
            ) {
                Text(
                    modifier = Modifier
                        .alpha(animateFloatAsState(targetValue = if (name.isEmpty()) 0.5f else 1f).value)
                        .padding(start = 2.dp),
                    text = name.ifEmpty { "Account Name" },
                    style = MaterialTheme.typography.body1
                )
                Text(
                    modifier = Modifier.alpha(animateFloatAsState(targetValue = if (balance.isEmpty()) 0.65f else 1f).value),
                    text = if(balance.isEmpty()) "Account Balance" else "$balance $currency",
                    style = MaterialTheme.typography.body1.copy(
                        fontFamily = economica,
                        fontSize = 40.sp
                    )
                )
            }
            CardNumber(
                modifier = Modifier
                    .alpha(animateFloatAsState(targetValue = if (description.isEmpty()) 0.5f else 1f).value)
                    .align(Alignment.BottomStart)
                    .padding(start = 28.dp, bottom = 25.dp),
                lastDigits = description,
                visible = descriptionVisible
            )
            AccountIcon(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 20.dp, bottom = 20.dp),
                type = type
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
            .clip(RoundedCornerShape(20.dp))
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
    lastDigits: String,
    visible: Boolean = true
) {
    Box(modifier = modifier) {
        AnimatedVisibility(
            visible = visible,
            enter = fadeIn(tween(100)), exit = fadeOut(tween(100))
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    modifier = Modifier.offset(0.dp, (-2).dp),
                    text = "● ● ● ●",
                    style = TextStyle(
                        fontFamily = credit_card,
                        fontSize = 11.sp
                    )
                )
                Spacer(modifier = Modifier.width(if(lastDigits.isEmpty()) 5.dp else 4.dp))
                Text(
                    text = lastDigits,
                    style = TextStyle(
                        fontFamily = credit_card,
                        fontSize = 12.sp,
                        letterSpacing = 3.sp
                    )
                )
                if(lastDigits.isEmpty()) {
                    Text(
                        modifier = Modifier.offset(0.dp, (-2).dp),
                        text = "●",
                        style = TextStyle(
                            fontFamily = credit_card,
                            fontSize = 11.sp
                        )
                    )
                }
                if(lastDigits.length < 2) {
                    Text(
                        modifier = Modifier.offset(0.dp, (-2).dp),
                        text = " ●",
                        style = TextStyle(
                            fontFamily = credit_card,
                            fontSize = 11.sp
                        )
                    )
                }
                if(lastDigits.length < 3) {
                    Text(
                        modifier = Modifier.offset(0.dp, (-2).dp),
                        text = " ●",
                        style = TextStyle(
                            fontFamily = credit_card,
                            fontSize = 11.sp
                        )
                    )
                }
                if(lastDigits.length < 4) {
                    Text(
                        modifier = Modifier.offset(0.dp, (-2).dp),
                        text = " ●",
                        style = TextStyle(
                            fontFamily = credit_card,
                            fontSize = 11.sp
                        )
                    )
                }
            }
        }
    }


}

@Composable
private fun AccountIcon(
    modifier: Modifier = Modifier,
    type: AccountType,
    color: Color = MaterialTheme.colors.onBackground
) {
    Icon(
        modifier = modifier,
        imageVector = getAccountIcon(type),
        contentDescription = Icons.Default.CreditCard.name,
        tint = color
    )
}

fun getAccountIcon(type: AccountType) : ImageVector {
    return when(type) {
        AccountType.CARD -> Icons.Default.CreditCard
        AccountType.CASH -> Icons.Default.AccountBalanceWallet
        AccountType.SAVINGS -> Icons.Default.Savings
        AccountType.HELP -> Icons.Default.SupervisorAccount
        AccountType.INSURANCE -> Icons.Default.MonitorHeart
        AccountType.CRYPTO -> Icons.Default.CurrencyBitcoin
        AccountType.UNKNOWN -> Icons.Default.AccountBalance
    }
}
