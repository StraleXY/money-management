package com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation

import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.draw.drawWithContent
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.graphics.drawscope.clipRect
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.platform.LocalView
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.PagerState
import com.google.accompanist.pager.calculateCurrentOffsetForPage
import com.google.accompanist.pager.rememberPagerState
import com.google.android.material.math.MathUtils
import com.theminimalismhub.moneymanagement.core.enums.FundType
import com.theminimalismhub.moneymanagement.core.enums.RecurringType
import com.theminimalismhub.moneymanagement.feature_accounts.domain.model.Account
import com.theminimalismhub.moneymanagement.feature_categories.domain.model.Category
import com.theminimalismhub.moneymanagement.feature_funds.data.model.FundItem
import com.theminimalismhub.moneymanagement.feature_funds.domain.model.Fund
import com.theminimalismhub.moneymanagement.feature_funds.presentation.manage_funds.presentation.FundCards.DisplayFundCard
import kotlin.math.abs
import kotlin.math.absoluteValue
import kotlin.math.max

@OptIn(ExperimentalPagerApi::class)
@Composable
fun FundSelectorPager(
    modifier: Modifier = Modifier,
    name: String,
    amount: Double,
    accounts: List<Account>,
    categories: List<Category>,
    recurring: RecurringType?,
    initialType: FundType,
    enabled: Boolean = true,
    minAlpha: Float = 1f,
    initialCardScale: Float = 1.05f,
    selectedCardScale: Float = 1.15f,
    selectedCardStartScale: Float = 0.95f,
    cardSpacing: Dp = 0.dp,
    onTypeFundSelected: (FundType) -> Unit
) {

    val pagerState = rememberPagerState(3, initialType.value)
    var initialScroll: Boolean by remember { mutableStateOf(true) }
    val width = with(LocalDensity.current) { LocalView.current.width.toDp() }

    val messages = listOf(
        "Budget",
        "Reservation",
        "Savings"
    )

    LaunchedEffect(pagerState.currentPage) {
        if (!initialScroll) onTypeFundSelected(FundType.get(pagerState.currentPage) ?: FundType.BUDGET)
        else initialScroll = false
    }

    Column(modifier = Modifier.fillMaxWidth()) {

        Row(
            modifier = Modifier
                .padding(start = 24.dp)
                .fillMaxWidth()
        ) {
            WipeRevealText(
                messages = messages,
                pagerState = pagerState
            )
        }

        HorizontalPager(
            state = pagerState,
            modifier = modifier
                .fillMaxWidth()
                .height(300.dp)
                .padding(top = 40.dp, bottom = 40.dp),
            dragEnabled = enabled
        ) { idx ->
            Box(
                modifier = Modifier
                    .graphicsLayer {
                        val pageOffset = calculateCurrentOffsetForPage(idx).absoluteValue

                        MathUtils
                            .lerp(selectedCardStartScale, selectedCardScale, 1f - pageOffset.coerceIn(0f, 1f))
                            .also { scale ->
                                scaleX = scale
                                scaleY = scale
                            }

                        MathUtils
                            .lerp(minAlpha, 1f, 1f - pageOffset.coerceIn(0f, 1f))
                            .also { value -> alpha = value }
                    }
                    .padding(horizontal = cardSpacing)
            ) {
                Box(
                    modifier = Modifier
                        .width(((width - 112.dp) * initialCardScale))
                ) {
                    Box(
                        modifier = Modifier
                            .scale(0.85f)
                    ) {
                        DisplayFundCard(
                            fund = Fund(
                                item = FundItem(
                                    name = name,
                                    amount = amount,
                                    type = FundType.get(idx)!!,
                                    recurringType = recurring
                                ),
                                accounts = accounts,
                                categories = categories
                            )
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalPagerApi::class)
@Composable
fun WipeRevealText(
    width: Dp = 180.dp,
    messages: List<String>,
    pagerState: PagerState
) {

    val nextIndex = pagerState.targetPage ?: ((pagerState.currentPage + 1) % messages.size)

    Box(
        modifier = Modifier
            .height(36.dp)
            .clipToBounds()
    ) {
        // Bottom text (next one)
        Text(
            text = messages.getOrNull(nextIndex) ?: "Unknown",
            style = MaterialTheme.typography.h4.copy(fontSize = 26.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .width(width)
                .align(Alignment.Center)
                .padding(
                    end = 0.dp,
                    start = max(24 * (if(pagerState.currentPageOffset >= 0) 2 - abs(pagerState.currentPageOffset) else abs(pagerState.currentPageOffset)), 0f).dp
                )
                .graphicsLayer {
                    clip = true
                    shape = RectangleShape
                }
                .drawWithContent {
                    val wipeWidth = size.width * (1 - abs(pagerState.currentPageOffset))
                    clipRect(
                        left = if(pagerState.currentPageOffset >= 0) wipeWidth else 0f ,
                        right = if(pagerState.currentPageOffset >= 0) size.width else size.width * abs(pagerState.currentPageOffset),
                        top = 0f,
                        bottom = size.height
                    ) {
                        this@drawWithContent.drawContent()
                    }
                },
            color = MaterialTheme.colors.onBackground
        )
        // Top text with wipe animation (current one being revealed away)
        Text(
            text = messages.getOrNull(pagerState.currentPage) ?: "Unknown",
            style = MaterialTheme.typography.h4.copy(fontSize = 26.sp, fontWeight = FontWeight.Medium),
            modifier = Modifier
                .width(width)
                .align(Alignment.Center)
                .padding(
                    end = max(24 * abs(pagerState.currentPageOffset), 0f).dp,
                    start = max(24 * (if(pagerState.currentPageOffset >= 0) 1 - abs(pagerState.currentPageOffset) else 1 + abs(pagerState.currentPageOffset)), 0f).dp
                )
                .alpha(if(pagerState.currentPageOffset == 0f) 1f else 1 - abs(pagerState.currentPageOffset))
                .graphicsLayer {
                    clip = true
                    shape = RectangleShape
                }
                .drawWithContent {
                    val wipeWidth = size.width * (1 - abs(pagerState.currentPageOffset))
                    clipRect(
                        left = if(pagerState.currentPageOffset >= 0) 0f else size.width,
                        right = if(pagerState.currentPageOffset >= 0) wipeWidth else size.width - wipeWidth,
                        top = 0f,
                        bottom = size.height
                    ) {
                        this@drawWithContent.drawContent()
                    }
                },
            color = MaterialTheme.colors.onBackground
        )
    }
}
