package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.drawBehind
import androidx.compose.ui.geometry.CornerRadius
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.android.material.math.MathUtils
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountCardLarge
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
fun AccountsPager(
    modifier: Modifier = Modifier,
    accounts: List<Account>,
    pagerState: PagerState,
    onAccountSelected: (Int) -> Unit
) {

    LaunchedEffect(pagerState.currentPage) {
        onAccountSelected(pagerState.currentPage)
    }

    HorizontalPager(
        state = pagerState,
        modifier = modifier
            .fillMaxWidth()
            .padding(top = 40.dp, bottom = 40.dp)
    ) {itemIdx ->
        if(accounts.isEmpty()) return@HorizontalPager
        Box(
            modifier = Modifier
                .graphicsLayer {
                    val pageOffset =
                        calculateCurrentOffsetForPage(itemIdx).absoluteValue

                    MathUtils.lerp(
                        0.95f, 1.15f, 1f - pageOffset.coerceIn(0f, 1f)
                    ).also { scale ->
                        scaleX = scale
                        scaleY = scale
                    }
                }
                .padding(horizontal = 12.dp)
        ) {
            AccountCardLarge(
                account = accounts[itemIdx],
                scale = 1.05f,
                overlayStrength = 0.1f
            )
        }
    }
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun AccountActions(
    enabled: Boolean = true,
    account: Account?,
    onToggleActivate: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        CircularActionButton(
            icon = Icons.Default.SyncAlt,
            action = "Transfer",
            enabled = enabled
        ) { }
        CircularActionButton(
            modifier = Modifier
                .alpha(animateFloatAsState(targetValue = if(enabled && !(account?.primary ?: true)) 1f else 0.65f, tween(200)).value),
            icon = Icons.Default.CreditScore,
            action = "Set As Primary",
            enabled = enabled && !(account?.primary ?: true)
        ) { }
        CircularActionButton(
            icon = if(account?.active != false) Icons.Default.RemoveShoppingCart else Icons.Default.AddShoppingCart,
            action = if(account?.active != false) "Disable" else "Enable",
            enabled = enabled,
            onClick = onToggleActivate
        )
        DashedCircularActionButton(
            icon = Icons.Default.AddCard,
            action = "Add Account",
            enabled = enabled
        ) { }
    }
    Spacer(modifier = Modifier.height(16.dp))
}

@Composable
fun CircularActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    action: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .background(
                    MaterialTheme.colors.surface,
                    RoundedCornerShape(100)
                )
                .clip(RoundedCornerShape(100))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = if(enabled) LocalIndication.current else null
                ) { if(enabled) onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = icon.name
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = action,
            style = MaterialTheme.typography.button.copy(
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.65.sp,
                fontSize = 14.sp
            ),
            color = MaterialTheme.colors.onBackground
        )
    }
}

@Composable
fun DashedCircularActionButton(
    modifier: Modifier = Modifier,
    strokeColor: Color = MaterialTheme.colors.secondary,
    icon: ImageVector,
    action: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    val stroke = Stroke(
        width = 2.5f,
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 12f), 0.1f)
    )

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(70.dp)
                .drawBehind {
                    drawRoundRect(
                        color = strokeColor,
                        style = stroke,
                        cornerRadius = CornerRadius(40.dp.toPx())
                    )
                }
                .clip(RoundedCornerShape(100))
                .clickable(
                    interactionSource = remember { MutableInteractionSource() },
                    indication = if(enabled) LocalIndication.current else null
                ) { if(enabled) onClick() },
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = icon.name
            )
        }
        Spacer(modifier = Modifier.height(12.dp))
        Text(
            text = action,
            style = MaterialTheme.typography.button.copy(
                fontWeight = FontWeight.Normal,
                letterSpacing = 0.65.sp,
                fontSize = 14.sp
            ),
            color = MaterialTheme.colors.onBackground
        )
    }
}