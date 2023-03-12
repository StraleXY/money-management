package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.LocalIndication
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
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
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.graphics.ColorUtils
import androidx.hilt.navigation.compose.hiltViewModel
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.android.material.math.MathUtils.lerp
import com.ramcosta.composedestinations.annotation.Destination
import com.theminimalismhub.moneymanagement.core.composables.ScreenHeader
import com.theminimalismhub.moneymanagement.core.transitions.BaseTransition
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AccountCardLarge
import com.theminimalismhub.moneymanagement.feature_accounts.presentation.composables.AddNewAccount
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
@Destination(style = BaseTransition::class)
fun ManageAccountsScreen(
    vm: ManageAccountsViewModel = hiltViewModel()
) {

    val state = vm.state.value

    val pagerState = rememberPagerState(
        pageCount = state.accounts.size,
        initialOffscreenLimit = 1,
    )

    LaunchedEffect(pagerState.currentPage) {
        vm.onEvent(ManageAccountsEvent.CardSelected(pagerState.currentPage))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            item {
                ScreenHeader(
                    title = "Management Accounts",
                    hint = "Track your balance across multiple accounts!"
                )
                HorizontalPager(
                    state = pagerState,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(top = 40.dp, bottom = 40.dp)
                ) {itemIdx ->
                    if(state.accounts.isEmpty()) return@HorizontalPager
                    Box(
                        modifier = Modifier
                            .graphicsLayer {
                                val pageOffset =
                                    calculateCurrentOffsetForPage(itemIdx).absoluteValue

                                lerp(
                                    0.95f, 1.15f, 1f - pageOffset.coerceIn(0f, 1f)
                                ).also { scale ->
                                    scaleX = scale
                                    scaleY = scale
                                }
                            }
                            .padding(horizontal = 12.dp)
                    ) {
                        AccountCardLarge(
                            account = state.accounts[itemIdx],
                            scale = 1.05f,
                            overlayStrength = 0.1f
                        )
                    }
                }
                Spacer(modifier = Modifier.height(8.dp))
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    CircularActionButton(
                        icon = Icons.Default.SyncAlt,
                        action = "Transfer",
                        enabled = !pagerState.isScrollInProgress
                    ) { }
                    CircularActionButton(
                        icon = Icons.Default.CreditScore,
                        action = "Set As Primary",
                        enabled = !pagerState.isScrollInProgress && !(state.selectedAccount?.primary ?: true)
                    ) { }
                    CircularActionButton(
                        icon = if(state.selectedAccount?.active != false) Icons.Default.RemoveShoppingCart else Icons.Default.AddShoppingCart,
                        action = if(state.selectedAccount?.active != false) "Disable" else "Enable",
                        enabled = !pagerState.isScrollInProgress
                    ) { vm.onEvent(ManageAccountsEvent.ToggleActive) }
                    DashedCircularActionButton(
                        icon = Icons.Default.AddCard,
                        action = "Add Account",
                        enabled = !pagerState.isScrollInProgress
                    ) { }
                }
            }
        }
    }
}

@Composable
private fun CircularActionButton(
    modifier: Modifier = Modifier,
    icon: ImageVector,
    action: String,
    enabled: Boolean = true,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .alpha(animateFloatAsState(targetValue = if(enabled) 1f else 0.65f, tween(75)).value),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
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
private fun DashedCircularActionButton(
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
        modifier = Modifier
            .alpha(animateFloatAsState(targetValue = if(enabled) 1f else 0.65f, tween(75)).value),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = modifier
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