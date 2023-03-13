package com.theminimalismhub.moneymanagement.feature_accounts.presentation.manage_accounts

import android.util.Log
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.runtime.*
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
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.layout.positionInRoot
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
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
import kotlin.math.abs
import kotlin.math.absoluteValue

@OptIn(ExperimentalPagerApi::class)
@Composable
@Destination(style = BaseTransition::class)
fun ManageAccountsScreen(
    vm: ManageAccountsViewModel = hiltViewModel()
) {

    val state = vm.state.value
    val density = LocalDensity.current.density

    val pagerState = rememberPagerState(
        pageCount = state.accounts.size,
        initialOffscreenLimit = 1,
    )

    var headerHeight by remember { mutableStateOf(0.dp) }
    var accountsPagerHeight by remember { mutableStateOf(0.dp) }
    var contentHeight by remember { mutableStateOf(0.dp) }
    val scroll: ScrollState = rememberScrollState(0)

    LaunchedEffect(scroll.isScrollInProgress) {
        if(scroll.isScrollInProgress) return@LaunchedEffect
        if(scroll.value.dp < 300.dp) scroll.animateScrollTo(0, tween(250))
        else if(scroll.value < ((headerHeight - accountsPagerHeight - 48.dp).value * density).toInt()) scroll.animateScrollTo(((headerHeight - accountsPagerHeight - 48.dp).value * density).toInt(), tween(250))
    }

    LaunchedEffect(pagerState.currentPage) {
        vm.onEvent(ManageAccountsEvent.CardSelected(pagerState.currentPage))
    }

    Box(modifier = Modifier.fillMaxSize()) {
        LazyColumn(
            contentPadding = PaddingValues(bottom = 20.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .graphicsLayer {
                    translationY =
                        -(scroll.value.toFloat()).coerceAtMost(173.dp.toPx())
                }
        ) {
            item {
                Column (
                    modifier = Modifier.onSizeChanged { headerHeight = Dp(it.height / density) }
                ) {
                    ScreenHeader(
                        title = "Management Accounts",
                        hint = "Track your balance across multiple accounts!"
                    )
                    HorizontalPager(
                        state = pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .onSizeChanged { accountsPagerHeight = Dp(it.height / density) }
                            .padding(top = 40.dp, bottom = 40.dp)
                    ) { itemIdx ->
                        if (state.accounts.isEmpty()) return@HorizontalPager
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
                            modifier = Modifier
                                .alpha(
                                    animateFloatAsState(
                                        targetValue = if (!pagerState.isScrollInProgress && !(state.selectedAccount?.primary
                                                ?: true)
                                        ) 1f else 0.65f, tween(200)
                                    ).value
                                ),
                            icon = Icons.Default.CreditScore,
                            action = "Set As Primary",
                            enabled = !pagerState.isScrollInProgress && !(state.selectedAccount?.primary
                                ?: true)
                        ) { }
                        CircularActionButton(
                            icon = if (state.selectedAccount?.active != false) Icons.Default.RemoveShoppingCart else Icons.Default.AddShoppingCart,
                            action = if (state.selectedAccount?.active != false) "Disable" else "Enable",
                            enabled = !pagerState.isScrollInProgress
                        ) { vm.onEvent(ManageAccountsEvent.ToggleActive) }
                        DashedCircularActionButton(
                            icon = Icons.Default.AddCard,
                            action = "Add Account",
                            enabled = !pagerState.isScrollInProgress
                        ) { }
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                }
            }
        }
        Column(
            modifier = Modifier
//                .heightIn(Dp(LocalView.current.height / density - headerHeight.value + accountsPagerHeight.value))
                .graphicsLayer {
                    translationY = -((scroll.value.dp / 2 - headerHeight)
                        .coerceAtLeast(-headerHeight)
                        .coerceAtMost(-accountsPagerHeight)).toPx()
                }
                .verticalScroll(scroll)) {
            Column(
                modifier = Modifier
                    .background(Color(0XFF212121))
                    .graphicsLayer {
                        translationY =
                            if (scroll.value.toFloat() < (headerHeight - accountsPagerHeight - 48.dp).toPx()) scroll.value.toFloat() else -(scroll.value.toFloat() - (headerHeight - accountsPagerHeight - 48.dp).toPx() * 3) / 2
                    }
                ,
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Content
                Column(
                    modifier = Modifier
                        .onSizeChanged { contentHeight = Dp(it.height / density) }
                ) {
                    repeat(4) {
                        Text(
                            text = "In the modern design world, Lorem Ipsum is the industry standard when placing dummy text onto an unfinished page, whether it's a newspaper, magazine, or advertisement. The Latin text was first used in the 16th century, when a printer scrambled a row of type (known as a \"galley\") so it could be used in a book that showcased the type's quality. This text saw a resurgence when electronic typesetting became popular in the 1960s, mainly because the French typography company Letraset started selling sheets with Lorem Ipsum.",
                            style = MaterialTheme.typography.body1,
                            textAlign = TextAlign.Justify,
                            modifier = Modifier
                                .padding(16.dp)
                        )
                    }
                    Button(onClick = { /*TODO*/ }) {
                        Text("Test", style = MaterialTheme.typography.button)
                    }
                }
                Spacer(modifier = Modifier.height(headerHeight))
                Spacer(modifier = Modifier.height(headerHeight))
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
                    indication = if (enabled) LocalIndication.current else null
                ) { if (enabled) onClick() },
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
                    indication = if (enabled) LocalIndication.current else null
                ) { if (enabled) onClick() },
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